package com.test.ecommerceorderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String fullName;
    private String password;
    private String email;
    private Role role;
    private String token;
    private LocalDateTime tokenExpiration;
}
