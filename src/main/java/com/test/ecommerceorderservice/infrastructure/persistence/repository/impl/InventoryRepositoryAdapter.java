package com.test.ecommerceorderservice.infrastructure.persistence.repository.impl;

import com.test.ecommerceorderservice.application.mapper.InventoryMapper;
import com.test.ecommerceorderservice.domain.model.Inventory;
import com.test.ecommerceorderservice.domain.repository.InventoryRepository;
import com.test.ecommerceorderservice.infrastructure.persistence.entity.InventoryEntity;
import com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa.InventoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final InventoryJpaRepository inventoryJpaRepository;

    @Override
    @Transactional
    public Inventory save(Inventory inventory) {
        InventoryEntity inventoryEntity = InventoryMapper.toEntity(inventory);
        InventoryEntity savedEntity = inventoryJpaRepository.save(inventoryEntity);
        return InventoryMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Inventory> findByProductId(Long productId) {
        return inventoryJpaRepository.findByProductId(productId)
                .map(InventoryMapper::toDomain);
    }

    @Override
    public Optional<Inventory> findById(Long id) {
        return inventoryJpaRepository.findById(id)
                .map(InventoryMapper::toDomain);
    }

    @Override
    public Page<Inventory> findAll(String search, Pageable pageable) {
        if (search == null || search.isBlank()) {
            return inventoryJpaRepository.findAll(pageable)
                    .map(InventoryMapper::toDomain);
        } else {
            return inventoryJpaRepository.findAllWithSearch(search, pageable)
                    .map(InventoryMapper::toDomain);
        }
    }

    @Override
    @Transactional
    public int updateQuantity(Long inventoryId, Integer quantity) {
        return inventoryJpaRepository.setQuantity(
                inventoryId,
                quantity
        );
    }

    @Override
    @Transactional
    public int decreaseIfEnough(Long productId, Integer quantity) {
        return inventoryJpaRepository.decreaseIfEnough(productId, quantity);
    }

    @Override
    @Transactional
    public int increaseByProduct(Long productId, Integer quantity) {
        return inventoryJpaRepository.increaseByProduct(productId, quantity);
    }

    @Override
    public boolean existsByProductId(Long productId) {
        return inventoryJpaRepository.existsByProductId(productId);
    }

    @Override
    public boolean existsById(Long id) {
        return inventoryJpaRepository.existsById(id);
    }
}

