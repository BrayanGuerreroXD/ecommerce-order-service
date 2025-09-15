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

    public User(Long id, String fullName, String mail, String pass, Role role) {
        this.id = id;
        this.fullName = fullName;
        this.email = mail;
        this.password = pass;
        this.role = role;
    }
}
