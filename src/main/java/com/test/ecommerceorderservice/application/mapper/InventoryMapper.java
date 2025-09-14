package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.application.dto.response.InventoryResponse;
import com.test.ecommerceorderservice.domain.model.Inventory;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.InventoryEntity;

public class InventoryMapper {

    public static InventoryEntity toEntity(Inventory inventory) {
        if (inventory == null) return null;
        InventoryEntity entity = new InventoryEntity();
        entity.setId(inventory.getId());
        entity.setProduct(ProductMapper.toEntity(inventory.getProduct()));
        entity.setQuantity(inventory.getQuantity());
        return entity;
    }

    public static Inventory toDomain(InventoryEntity entity) {
        if (entity == null) return null;
        Inventory inventory = new Inventory();
        inventory.setId(entity.getId());
        inventory.setProduct(ProductMapper.toDomain(entity.getProduct()));
        inventory.setQuantity(entity.getQuantity());
        return inventory;
    }

    public static InventoryResponse toResponse(Inventory inventory) {
        if (inventory == null) return null;
        InventoryResponse response = new InventoryResponse();
        response.setId(inventory.getId());
        response.setProduct(ProductMapper.toResponse(inventory.getProduct()));
        response.setQuantity(inventory.getQuantity());
        return response;
    }
}

