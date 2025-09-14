package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.application.dto.response.OrderResponse;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.model.enums.OrderStatus;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.OrderEntity;

public class OrderMapper {

    public static OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }
        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setUser(UserMapper.toEntity(order.getUser()));
        entity.setProduct(ProductMapper.toEntity(order.getProduct()));
        entity.setQuantity(order.getQuantity());
        entity.setTotalPrice(order.getTotalPrice());
        entity.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        entity.setCreatedAt(order.getCreatedAt());
        return entity;
    }

    public static Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        Order order = new Order();
        order.setId(entity.getId());
        order.setUser(UserMapper.toDomain(entity.getUser()));
        order.setProduct(ProductMapper.toDomain(entity.getProduct()));
        order.setQuantity(entity.getQuantity());
        order.setTotalPrice(entity.getTotalPrice());
        order.setStatus(entity.getStatus() != null ? Enum.valueOf(OrderStatus.class, entity.getStatus()) : null);
        order.setCreatedAt(entity.getCreatedAt());
        return order;
    }

    public static OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setUserId(order.getUser() != null ? order.getUser().getId() : null);
        response.setProductId(order.getProduct() != null ? order.getProduct().getId() : null);
        response.setQuantity(order.getQuantity());
        response.setTotalPrice(order.getTotalPrice());
        response.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        response.setCreatedAt(order.getCreatedAt());
        return response;
    }
}

