package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.response.OrderLogResponse;
import com.test.ecommerceorderservice.application.mapper.OrderLogMapper;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.model.OrderLog;
import com.test.ecommerceorderservice.domain.repository.OrderLogRepository;
import com.test.ecommerceorderservice.shared.util.ObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderLogApplicationService {

    private final OrderLogRepository orderLogRepository;
    private final ObjectMapperUtil objectMapperUtil;

    @Transactional
    public void saveOrderLog(Order order) {
        log.info("Saving order log for orderId {}", order.getId());
        OrderLog orderLog = new OrderLog();
        orderLog.setOrder(order);
        orderLog.setDetails(ObjectMapperUtil.toMap(order));
        orderLogRepository.save(orderLog);
    }

    public List<OrderLogResponse> findAllOrderLogResponseByOrderId(Long orderId) {
        log.info("Fetching order logs for orderId {}", orderId);
        List<OrderLog> orderLogs = orderLogRepository.findAllByOrderId(orderId);
        return orderLogs.stream().map(OrderLogMapper::toResponse).toList();
    }
}