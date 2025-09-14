package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.response.OrderLogResponse;
import com.test.ecommerceorderservice.application.mapper.OrderLogMapper;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.model.OrderLog;
import com.test.ecommerceorderservice.domain.repository.OrderLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLogApplicationService {

    private final OrderLogRepository orderLogRepository;

    @Transactional
    public void saveOrderLog(Order order) {
        log.info("Saving order log for orderId {}", order.getId());
        OrderLog orderLog = new OrderLog();
        orderLog.setOrder(order);
        orderLog.setDetails(this.toMap(order));
        orderLogRepository.save(orderLog);
    }

    public List<OrderLogResponse> findAllOrderLogResponseByOrderId(Long orderId) {
        log.info("Fetching order logs for orderId {}", orderId);
        List<OrderLog> orderLogs = orderLogRepository.findAllByOrderId(orderId);
        return orderLogs.stream().map(OrderLogMapper::toResponse).toList();
    }

    private Map<String, Object> toMap(Order order) {
        Map<String, Object> details = new HashMap<>();
        Map<String, Object> orderDetails = new HashMap<>();
        Map<String, Object> userDetails = new HashMap<>();
        Map<String, Object> productDetails = new HashMap<>();

        orderDetails.put("id", order.getId());
        orderDetails.put("status", order.getStatus());
        orderDetails.put("quantity", order.getQuantity());
        orderDetails.put("totalPrice", order.getTotalPrice());

        if (order.getUser() != null) {
            userDetails.put("id", order.getUser().getId());
            userDetails.put("name", order.getUser().getFullName());
            userDetails.put("email", order.getUser().getEmail());
        }

        if (order.getProduct() != null) {
            productDetails.put("id", order.getProduct().getId());
            productDetails.put("name", order.getProduct().getName());
            productDetails.put("sku", order.getProduct().getSku());
            productDetails.put("price", order.getProduct().getPrice());
        }

        details.put("order", orderDetails);
        details.put("user", userDetails);
        details.put("product", productDetails);
        return details;
    }
}