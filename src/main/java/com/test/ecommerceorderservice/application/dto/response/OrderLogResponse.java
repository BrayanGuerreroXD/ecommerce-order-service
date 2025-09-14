package com.test.ecommerceorderservice.application.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class OrderLogResponse {
    private Long id;
    private OrderResponse order;
    private Map<String, Object> details;
    private LocalDateTime createdAt;
}