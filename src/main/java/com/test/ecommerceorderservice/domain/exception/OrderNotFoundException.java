package com.test.ecommerceorderservice.domain.exception;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String message) {
        super(message);
    }
}

