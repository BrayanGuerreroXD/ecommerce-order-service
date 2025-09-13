package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.RoleMapper;
import com.test.ecommerceorderservice.domain.model.Role;
import com.test.ecommerceorderservice.domain.repository.RoleRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.RoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;


    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(RoleMapper::toDomainRole);
    }

    @Override
    public Optional<Role> findByKey(String key) {
        return roleJpaRepository.findByKey(key).map(RoleMapper::toDomainRole);
    }

    @Override
    public List<Role> findAll() {
        return roleJpaRepository.findAll().stream().map(RoleMapper::toDomainRole).toList();
    }
}