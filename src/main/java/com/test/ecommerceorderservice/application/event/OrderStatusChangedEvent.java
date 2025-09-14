package com.test.ecommerceorderservice.application.event;

import com.test.ecommerceorderservice.domain.model.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusChangedEvent implements DomainEvent {
    private final Long aggregateId;
    private final String type;
    private final Order order;
}