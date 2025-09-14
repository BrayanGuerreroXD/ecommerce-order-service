package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.OrderMapper;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.repository.OrderRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.OrderEntity;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = OrderMapper.toEntity(order);
        OrderEntity savedEntity = orderJpaRepository.save(entity);
        return OrderMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderJpaRepository.findAll(pageable).map(OrderMapper::toDomain);
    }

    @Override
    public Page<Order> findAllByUserId(Long userId, Pageable pageable) {
        return orderJpaRepository.findByUserId(userId, pageable).map(OrderMapper::toDomain);
    }

    @Override
    public int existsById(Long id) {
        return orderJpaRepository.existsById(id) ? 1 : 0;
    }
}