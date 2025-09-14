package com.test.ecommerceorderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inventory {
    private Long id;
    private Product product;
    private Integer quantity;
    private Long version;
}