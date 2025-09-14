package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.ProductMapper;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.repository.ProductRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.ProductEntity;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    @Transactional
    public Product save(Product product) {
        ProductEntity entity = ProductMapper.toEntity(product);
        ProductEntity savedEntity = productJpaRepository.save(entity);
        return ProductMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(ProductMapper::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return productJpaRepository.findBySku(sku).map(ProductMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream().map(ProductMapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }
}