package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        User user = new User();
        user.setId(entity.getId());
        user.setFullName(entity.getFullName());
        user.setPassword(entity.getPassword());
        user.setEmail(entity.getEmail());
        user.setRole(RoleMapper.toDomainRole(entity.getRole()));
        user.setToken(entity.getToken());
        user.setTokenExpiration(entity.getTokenExpiration());
        return user;
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setFullName(user.getFullName());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setRole(RoleMapper.toEntityRole(user.getRole()));
        entity.setToken(user.getToken());
        entity.setTokenExpiration(user.getTokenExpiration());
        return entity;
    }
}
