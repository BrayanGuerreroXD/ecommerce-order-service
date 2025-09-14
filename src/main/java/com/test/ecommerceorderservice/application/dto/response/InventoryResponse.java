package com.test.ecommerceorderservice.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InventoryResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
}