package com.test.ecommerceorderservice.shared.events;

import com.test.ecommerceorderservice.application.event.DomainEvent;

public interface DomainEventPublisher {
    void publish(DomainEvent event);
}