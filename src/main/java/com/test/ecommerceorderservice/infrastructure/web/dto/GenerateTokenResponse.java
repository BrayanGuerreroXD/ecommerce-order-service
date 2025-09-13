package com.test.ecommerceorderservice.infrastructure.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GenerateTokenResponse {
    private String token;
    private LocalDateTime expirationDate;
}