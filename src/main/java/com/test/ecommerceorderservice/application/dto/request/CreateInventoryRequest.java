package com.test.ecommerceorderservice.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class CreateInventoryRequest {
    @NotNull(message = "Product ID cannot be null")
    @Positive(message = "Product ID must be greater than zero")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;
}