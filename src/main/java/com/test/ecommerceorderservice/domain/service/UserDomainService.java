package com.test.ecommerceorderservice.domain.service;

import com.test.ecommerceorderservice.domain.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {
    // Aquí puedes agregar lógica de negocio relacionada con usuarios
    public boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
}

