package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.application.dto.response.OrderLogResponse;
import com.test.ecommerceorderservice.domain.model.OrderLog;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.OrderLogEntity;

public class OrderLogMapper {
    public static OrderLogEntity toEntity(OrderLog orderLog) {
        if (orderLog == null) {
            return null;
        }
        OrderLogEntity entity = new OrderLogEntity();
        entity.setId(orderLog.getId());
        entity.setOrder(OrderMapper.toEntity(orderLog.getOrder()));
        entity.setDetails(orderLog.getDetails());
        entity.setCreatedAt(orderLog.getCreatedAt());
        return entity;
    }

    public static OrderLog toDomain(OrderLogEntity entity) {
        if (entity == null) {
            return null;
        }
        OrderLog orderLog = new OrderLog();
        orderLog.setId(entity.getId());
        orderLog.setOrder(OrderMapper.toDomain(entity.getOrder()));
        orderLog.setDetails(entity.getDetails());
        orderLog.setCreatedAt(entity.getCreatedAt());
        return orderLog;
    }

    public static OrderLogResponse toResponse(OrderLog orderLog) {
        if (orderLog == null) {
            return null;
        }
        OrderLogResponse response = new OrderLogResponse();
        response.setId(orderLog.getId());
        response.setOrder(OrderMapper.toResponse(orderLog.getOrder()));
        response.setDetails(orderLog.getDetails());
        response.setCreatedAt(orderLog.getCreatedAt());
        return response;
    }

}
