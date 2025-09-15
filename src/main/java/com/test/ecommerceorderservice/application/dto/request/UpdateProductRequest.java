package com.test.ecommerceorderservice.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProductRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "SKU is required")
    private String sku;
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to zero")
    private Double price;
}
