package com.test.ecommerceorderservice.domain.repository;

import com.test.ecommerceorderservice.domain.model.OrderLog;

import java.util.List;

public interface OrderLogRepository {
    OrderLog save(OrderLog orderLog);
    List<OrderLog> findAllByOrderId(Long orderId);
}