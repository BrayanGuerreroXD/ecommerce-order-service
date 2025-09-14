package com.test.ecommerceorderservice.application.mapper;

import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.ProductEntity;

public class ProductMapper {

    public static ProductEntity toEntity(Product product) {
        if (product == null) return null;
        ProductEntity entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        entity.setSku(product.getSku());
        return entity;
    }

    public static Product toDomain(ProductEntity entity) {
        if (entity == null) return null;
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setPrice(entity.getPrice());
        product.setSku(entity.getSku());
        return product;
    }
}

