package com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa;

import com.test.ecommerceorderservice.infrastructure.persistence.entity.InventoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, Long> {
    Optional<InventoryEntity> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    @Query("""
        SELECT i FROM InventoryEntity i
        WHERE :search IS NULL
           OR i.product.name LIKE CONCAT('%', :search, '%')
           OR i.product.sku LIKE CONCAT('%', :search, '%')
    """)
    Page<InventoryEntity> findAllWithSearch(String search, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE InventoryEntity i SET i.quantity = :newQty WHERE i.id = :id")
    int setQuantity(@Param("id") Long id, @Param("newQty") int newQty);

    @Modifying
    @Transactional
    @Query("UPDATE InventoryEntity i SET i.quantity = i.quantity - :qty WHERE i.product.id = :productId AND i.quantity >= :qty")
    int decreaseIfEnough(@Param("productId") Long productId, @Param("qty") int qty);

    @Modifying
    @Transactional
    @Query("UPDATE InventoryEntity i SET i.quantity = i.quantity + :qty WHERE i .product.id = :productId")
    int increaseByProduct(@Param("productId") Long productId, @Param("qty") int qty);

}