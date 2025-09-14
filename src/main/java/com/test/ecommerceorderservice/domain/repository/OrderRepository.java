package com.test.ecommerceorderservice.domain.repository;

import com.test.ecommerceorderservice.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);
    Optional<Order> findById(Long id);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findAllByUserId(Long userId, Pageable pageable);
    int existsById(Long id);
}

