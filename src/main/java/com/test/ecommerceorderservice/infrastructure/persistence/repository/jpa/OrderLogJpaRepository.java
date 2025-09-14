package com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa;

import com.test.ecommerceorderservice.infrastructure.persistence.entity.OrderLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLogJpaRepository extends JpaRepository<OrderLogEntity, Long> {
    List<OrderLogEntity> findByOrderId(Long orderId);
}