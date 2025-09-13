package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;
        return new User(
                entity.getId(),
                entity.getFullName(),
                entity.getPassword(),
                entity.getEmail(),
                RoleMapper.toDomainRole(entity.getRole())
        );
    }

    public static UserEntity toEntity(User user) {
        if (user == null) return null;
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setFullName(user.getFullName());
        entity.setPassword(user.getPassword());
        entity.setEmail(user.getEmail());
        entity.setRole(RoleMapper.toEntityRole(user.getRole()));
        return entity;
    }
}
