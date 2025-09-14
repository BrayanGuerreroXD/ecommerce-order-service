package com.test.ecommerceorderservice.application.event;

import com.test.ecommerceorderservice.domain.model.Order;

public interface DomainEvent {
    Long getAggregateId();
    String getType();
    Order getOrder();
}