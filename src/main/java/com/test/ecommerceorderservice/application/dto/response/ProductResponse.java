package com.test.ecommerceorderservice.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String sku;
    private String name;
    private Double price;
}