package com.test.ecommerceorderservice.application.port.out;

public interface EventPublisherPort {
    void publishEvent(Object event);
}

