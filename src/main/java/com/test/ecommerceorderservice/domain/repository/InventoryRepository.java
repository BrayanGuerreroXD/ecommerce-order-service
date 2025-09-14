package com.test.ecommerceorderservice.domain.repository;

import com.test.ecommerceorderservice.domain.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface InventoryRepository {
    Inventory save(Inventory inventory);
    Optional<Inventory> findByProductId(Long productId);
    Optional<Inventory> findById(Long id);
    Page<Inventory> findAll(String search, Pageable pageable);
    int updateQuantity(Long inventoryId, Integer quantity);
    int decreaseIfEnough(Long productId, Integer quantity);
    void deleteById(Long id);
    boolean existsByProductId(Long productId);
    boolean existsById(Long id);
}