package com.test.ecommerceorderservice.infrastructure.security.service;

import com.test.ecommerceorderservice.infrastructure.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandleCurrentSessionUserIdService {
    private final HttpServletService httpServletService;
    private final JwtTokenProvider jwtTokenProvider;

    public Long handleCurrentSessionUserId(HttpServletRequest tokenRequest) {
        String token = httpServletService.getToken(tokenRequest);
        return Long.parseLong(jwtTokenProvider.getUserIdFromJWT(token));
    }
}