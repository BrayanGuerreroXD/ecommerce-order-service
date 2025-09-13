package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.request.LoginRequest;
import com.test.ecommerceorderservice.application.dto.response.LoginResponse;
import com.test.ecommerceorderservice.infrastructure.security.service.AuthService;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthApplicationService {

    private final AuthService authService;

    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        return authService.login(
                loginRequest.getEmail(),
                loginRequest.getPassword()
        );
    }

    @Transactional
    public SuccessResponse closeSession(HttpServletRequest request) {
        return authService.closeSession(request);
    }
}