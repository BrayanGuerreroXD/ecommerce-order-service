package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.response.OrderLogResponse;
import com.test.ecommerceorderservice.application.service.OrderLogApplicationService;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.model.OrderLog;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.domain.model.enums.OrderStatus;
import com.test.ecommerceorderservice.domain.repository.OrderLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLogApplicationServiceTest {

    @Mock
    private OrderLogRepository orderLogRepository;

    @InjectMocks
    private OrderLogApplicationService orderLogService;

    private Order order;

    @BeforeEach
    void setup() {
        User user = new User();
        user.setId(1L);
        user.setFullName("John Doe");
        user.setEmail("john@example.com");

        Product product = new Product();
        product.setId(10L);
        product.setName("Test Product");
        product.setSku("SKU-123");
        product.setPrice(50.0);

        order = new Order();
        order.setId(99L);
        order.setStatus(OrderStatus.CREATED);
        order.setQuantity(2);
        order.setTotalPrice(100.0);
        order.setUser(user);
        order.setProduct(product);
    }

    @Test
    void saveOrderLog_shouldCallRepositorySave() {
        orderLogService.saveOrderLog(order);

        ArgumentCaptor<OrderLog> captor = ArgumentCaptor.forClass(OrderLog.class);
        verify(orderLogRepository).save(captor.capture());

        OrderLog saved = captor.getValue();
        assertThat(saved.getOrder()).isEqualTo(order);
        assertThat(saved.getDetails()).isNotNull();
        assertThat(saved.getDetails()).containsKeys("order", "user", "product");
    }

    @Test
    void findAllOrderLogResponseByOrderId_shouldReturnMappedResponses() {
        OrderLog log = new OrderLog();
        log.setOrder(order);
        log.setDetails(orderLogServiceTestHelperDetails());

        when(orderLogRepository.findAllByOrderId(99L)).thenReturn(List.of(log));

        List<OrderLogResponse> responses = orderLogService.findAllOrderLogResponseByOrderId(99L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getDetails()).containsKeys("order", "user", "product");
        verify(orderLogRepository).findAllByOrderId(99L);
    }

    private static Map<String, Object> orderLogServiceTestHelperDetails() {
        return Map.of(
                "order", Map.of("id", 99L, "status", OrderStatus.CREATED, "quantity", 2, "totalPrice", 100.0),
                "user", Map.of("id", 1L, "name", "John Doe", "email", "john@example.com"),
                "product", Map.of("id", 10L, "name", "Test Product", "sku", "SKU-123", "price", 50.0)
        );
    }
}
