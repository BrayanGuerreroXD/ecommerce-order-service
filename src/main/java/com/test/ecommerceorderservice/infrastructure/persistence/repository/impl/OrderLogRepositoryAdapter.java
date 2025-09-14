package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.OrderLogMapper;
import com.test.ecommerceorderservice.domain.model.OrderLog;
import com.test.ecommerceorderservice.domain.repository.OrderLogRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.OrderLogEntity;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.OrderLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderLogRepositoryAdapter implements OrderLogRepository {

    private final OrderLogJpaRepository orderLogJpaRepository;

    @Override
    public OrderLog save(OrderLog orderLog) {
        OrderLogEntity entity = OrderLogMapper.toEntity(orderLog);
        OrderLogEntity savedEntity = orderLogJpaRepository.save(entity);
        return OrderLogMapper.toDomain(savedEntity);
    }

    @Override
    public List<OrderLog> findAllByOrderId(Long orderId) {
        return orderLogJpaRepository.findByOrderId(orderId).stream()
                .map(OrderLogMapper::toDomain)
                .toList();
    }
}
