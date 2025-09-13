package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.domain.model.Role;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.RoleEntity;

public class RoleMapper {
    public static Role toDomainRole(RoleEntity entity) {
        if (entity == null) return null;
        return new Role(entity.getId(), entity.getKey(), entity.getName());
    }

    public static RoleEntity toEntityRole(Role role) {
        if (role == null) return null;
        RoleEntity entity = new RoleEntity();
        entity.setId(role.getId());
        entity.setKey(role.getKey());
        entity.setName(role.getName());
        return entity;
    }
}
