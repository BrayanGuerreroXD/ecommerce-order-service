package com.test.ecommerceorderservice.infrastructure.security.service;

import com.test.ecommerceorderservice.infrastructure.persistence.repository.impl.UserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepositoryAdapter userRepositoryAdapter;

    public boolean authenticate(String email, String rawPassword) {
        return userRepositoryAdapter.findByEmail(email)
                .map(user -> new BCryptPasswordEncoder().matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
}
