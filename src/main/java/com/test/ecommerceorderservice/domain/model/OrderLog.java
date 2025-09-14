package com.test.ecommerceorderservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderLog {
    private Long id;
    private Order order;
    private Map<String, Object> details;
    private LocalDateTime createdAt;
}
