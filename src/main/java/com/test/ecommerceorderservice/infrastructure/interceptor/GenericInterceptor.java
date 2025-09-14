package com.test.ecommerceorderservice.infrastructure.interceptor;

import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.infrastructure.annotation.PublicIngress;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.enums.util.PublicPath;
import com.test.ecommerceorderservice.infrastructure.web.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericInterceptor implements HandlerInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // Handle public paths or ingress
        if (isPublicRequest(request, handlerMethod)) {
            return true;
        }

        User user = tokenValidator.validateTokenAndGetUserEntity(request);

        return isValidRole(handlerMethod, user);
    }

    private boolean isPublicRequest(HttpServletRequest request, HandlerMethod handlerMethod) {
        return PublicPath.containValue(request.getServletPath()) || this.getPublicIngress(handlerMethod) != null;
    }

    private boolean isValidRole(HandlerMethod handlerMethod, User user) {
        RoleVerify role = this.getRoleVerify(handlerMethod);

        if (Objects.isNull(role)) {
            return true;
        }

        boolean isValid = Arrays.stream(role.value())
                .anyMatch(r -> r.getAuthority().equalsIgnoreCase(user.getRole().getKey()));

        if (!isValid) {
            throw new UnauthorizedException(ExceptionCodeEnum.S01FORB01);
        }

        return true;
    }

    private PublicIngress getPublicIngress(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethod().getAnnotation(PublicIngress.class) != null) {
            return handlerMethod.getMethod().getAnnotation(PublicIngress.class);
        } else {
            return handlerMethod.getBean().getClass().getAnnotation(PublicIngress.class);
        }
    }

    private RoleVerify getRoleVerify(HandlerMethod handlerMethod) {
        if (handlerMethod.getMethod().getAnnotation(RoleVerify.class) != null) {
            return handlerMethod.getMethod().getAnnotation(RoleVerify.class);
        } else {
            return handlerMethod.getBean().getClass().getAnnotation(RoleVerify.class);
        }
    }
}
