package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.UserMapper;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.domain.repository.UserRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.UserEntity;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public User save(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return UserMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream().map(UserMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        userJpaRepository.deleteById(id);
    }
}