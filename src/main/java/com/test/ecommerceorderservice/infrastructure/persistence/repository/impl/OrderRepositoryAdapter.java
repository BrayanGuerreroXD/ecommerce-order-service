package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.repository.OrderRepository;
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
        return null;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Page<Order> findAllByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public int existsById(Long id) {
        return 0;
    }
}