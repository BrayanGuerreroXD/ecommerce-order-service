package com.test.ecommerceorderservice.infrastructure.security.service;

import com.test.ecommerceorderservice.application.dto.response.LoginResponse;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.impl.UserRepositoryAdapter;
import com.test.ecommerceorderservice.infrastructure.security.jwt.JwtTokenProvider;
import com.test.ecommerceorderservice.infrastructure.web.dto.GenerateTokenResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import com.test.ecommerceorderservice.infrastructure.web.exception.NotFoundException;
import com.test.ecommerceorderservice.infrastructure.web.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepositoryAdapter userRepositoryAdapter;
    private final JwtTokenProvider jwtTokenProvider;
    private final HttpServletService httpServletService;

    @Transactional
    public LoginResponse login(String email, String password) {
        User user = this.handleUserGetByEmail(email);

        if (!new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            throw new NotFoundException(ExceptionCodeEnum.S01LOGN01);
        }

        if (Objects.nonNull(user.getToken()) && user.getTokenExpiration().isAfter(LocalDateTime.now())) {
            throw new UnauthorizedException(ExceptionCodeEnum.S01UNAU08);
        }

        GenerateTokenResponse token = jwtTokenProvider.generateToken(user.getId().toString(), user.getRole().getKey());

        user.setToken(token.getToken());
        user.setTokenExpiration(token.getExpirationDate());
        userRepositoryAdapter.save(user);

        return new LoginResponse(
                user.getEmail(),
                token.getToken()
        );
    }

    @Transactional
    public SuccessResponse closeSession(HttpServletRequest request) {
        String token = httpServletService.getToken(request);

        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromJWT(token));
        User user = this. handleGetById(userId);

        user.setToken(null);
        user.setTokenExpiration(null);
        userRepositoryAdapter.save(user);

        return new SuccessResponse(true);
    }

    private User handleUserGetByEmail(String email) {
        return userRepositoryAdapter.findByEmail(email).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01USR01)
        );
    }

    private User handleGetById(Long id) {
        return userRepositoryAdapter.findById(id).orElseThrow(
                () -> new NotFoundException(ExceptionCodeEnum.C01USR01)
        );
    }
}
