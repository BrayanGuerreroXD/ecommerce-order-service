package com.test.ecommerceorderservice.infrastructure.interceptor;

import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.impl.UserRepositoryAdapter;
import com.test.ecommerceorderservice.infrastructure.security.jwt.JwtTokenProvider;
import com.test.ecommerceorderservice.infrastructure.security.service.HttpServletService;
import com.test.ecommerceorderservice.infrastructure.web.exception.NotFoundException;
import com.test.ecommerceorderservice.infrastructure.web.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenValidator {

    private final HttpServletService httpServletService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepositoryAdapter userRepositoryAdapter;

    public User validateTokenAndGetUserEntity(HttpServletRequest request) {
        String token = httpServletService.getToken(request);
        boolean isValidToken = jwtTokenProvider.validateToken(token);

        if (!isValidToken) {
            throw new UnauthorizedException(ExceptionCodeEnum.S01UNAU06);
        }

        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromJWT(token));

        return this.handleGetById(userId);
    }

    private User handleGetById(Long id) {
        return userRepositoryAdapter.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01USR01)
        );
    }
}