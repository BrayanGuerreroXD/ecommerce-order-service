package com.test.ecommerceorderservice.domain.exception;

public class InvalidOrderStateException extends DomainException {
    public InvalidOrderStateException(String message) {
        super(message);
    }
}

