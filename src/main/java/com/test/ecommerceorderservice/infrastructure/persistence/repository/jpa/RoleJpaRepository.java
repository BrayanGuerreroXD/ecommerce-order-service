package com.test.ecommerceorderservice.infrastructure.persistence.repository.jpa;

import com.test.ecommerceorderservice.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByKey(String key);
}

