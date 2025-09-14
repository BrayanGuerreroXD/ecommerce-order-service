package com.test.ecommerceorderservice.application.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderLogResponse {
    private Long id;
    private OrderResponse order;
    private Map<String, Object> details;
    private LocalDateTime createdAt;
}