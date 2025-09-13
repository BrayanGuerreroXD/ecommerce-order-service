package com.test.ecommerceorderservice.domain.repository;

import com.test.ecommerceorderservice.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findById(Long id);
    Optional<Role> findByKey(String key);
    List<Role> findAll();
}
