package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateOrderRequest;
import com.test.ecommerceorderservice.application.dto.response.OrderResponse;
import com.test.ecommerceorderservice.application.event.OrderStatusChangedEvent;
import com.test.ecommerceorderservice.application.service.InventoryApplicationService;
import com.test.ecommerceorderservice.application.service.OrderApplicationService;
import com.test.ecommerceorderservice.application.service.OrderLogApplicationService;
import com.test.ecommerceorderservice.application.service.ProductApplicationService;
import com.test.ecommerceorderservice.application.service.UserApplicationService;
import com.test.ecommerceorderservice.domain.model.Order;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.model.User;
import com.test.ecommerceorderservice.domain.model.enums.OrderStatus;
import com.test.ecommerceorderservice.domain.repository.OrderRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.BadRequestException;
import com.test.ecommerceorderservice.infrastructure.web.exception.ConflictException;
import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import com.test.ecommerceorderservice.shared.events.DomainEventPublisher;
import com.test.ecommerceorderservice.shared.util.PageableFilterConvert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderApplicationServiceTest {

    @InjectMocks
    private OrderApplicationService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PageableFilterConvert pageableFilterConvert;

    @Mock
    private DomainEventPublisher eventPublisher;

    @Mock
    private ProductApplicationService productApplicationService;

    @Mock
    private UserApplicationService userApplicationService;

    @Mock
    private InventoryApplicationService inventoryService;

    @Mock
    private OrderLogApplicationService orderLogApplicationService;

    private User user;
    private Product product;
    private Order order;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(10L);
        product.setPrice(100.0);

        order = new Order();
        order.setId(99L);
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(2);
        order.setStatus(OrderStatus.CREATED);
        order.setTotalPrice(200.0);
    }

    @Test
    void createOrder_success() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setProductId(product.getId());
        req.setQuantity(2);

        when(userApplicationService.getUserModelById(anyLong())).thenReturn(user);
        when(productApplicationService.getProductModelById(anyLong())).thenReturn(product);
        when(inventoryService.decreaseForOrder(eq(product.getId()), eq(req.getQuantity()))).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderLogApplicationService.findAllOrderLogResponseByOrderId(order.getId())).thenReturn(List.of());

        OrderResponse resp = orderService.createOrder(req, 1L);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(99L);

        verify(inventoryService).decreaseForOrder(product.getId(), 2);
        verify(orderRepository).save(any(Order.class));
        verify(orderLogApplicationService).saveOrderLog(order);
        verify(eventPublisher).publish(any(OrderStatusChangedEvent.class));
    }

    @Test
    void createOrder_outOfStock_throwsConflict() {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setProductId(product.getId());
        req.setQuantity(2);

        when(userApplicationService.getUserModelById(anyLong())).thenReturn(user);
        when(productApplicationService.getProductModelById(anyLong())).thenReturn(product);
        when(inventoryService.decreaseForOrder(eq(product.getId()), eq(2))).thenReturn(false);

        ConflictException ex = assertThrows(ConflictException.class, () -> orderService.createOrder(req, 1L));
        // comprueba código del error si tu ConflictException tiene getErrorResponse
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01ORD03.name());

        verify(orderRepository, never()).save(any());
    }

    @Test
    void payOrder_success() {
        order.setStatus(OrderStatus.CREATED);

        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderLogApplicationService.findAllOrderLogResponseByOrderId(order.getId())).thenReturn(List.of());

        OrderResponse resp = orderService.payOrder(order.getId(), user.getId());

        assertThat(resp.getStatus()).isEqualTo(OrderStatus.PAID.name());
        verify(orderRepository).save(order);
        verify(eventPublisher).publish(any(OrderStatusChangedEvent.class));
        verify(orderLogApplicationService).saveOrderLog(order);
    }

    @Test
    void payOrder_wrongStatus_throwsBadRequest() {
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> orderService.payOrder(order.getId(), user.getId()));
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01ORD02.name());
    }

    @Test
    void cancelOrder_success() {
        order.setStatus(OrderStatus.CREATED);
        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderLogApplicationService.findAllOrderLogResponseByOrderId(order.getId())).thenReturn(List.of());

        OrderResponse resp = orderService.cancelOrder(order.getId(), user.getId());

        assertThat(resp.getStatus()).isEqualTo(OrderStatus.CANCELED.name());
        verify(inventoryService).increaseByProduct(product.getId(), order.getQuantity());
        verify(orderLogApplicationService).saveOrderLog(order);
        verify(eventPublisher).publish(any(OrderStatusChangedEvent.class));
    }

    @Test
    void cancelOrder_paid_throwsBadRequest() {
        order.setStatus(OrderStatus.PAID);
        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> orderService.cancelOrder(order.getId(), user.getId()));
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01ORD05.name());
    }

    @Test
    void getOrderById_success() {
        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));
        when(orderLogApplicationService.findAllOrderLogResponseByOrderId(order.getId())).thenReturn(List.of());

        OrderResponse resp = orderService.getOrderById(order.getId(), user.getId());

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(order.getId());
    }

    @Test
    void getOrderById_wrongUser_throwsBadRequest() {
        User another = new User();
        another.setId(2L);
        order.setUser(another);

        when(orderRepository.findById(eq(order.getId()))).thenReturn(Optional.of(order));

        BadRequestException ex = assertThrows(BadRequestException.class, () -> orderService.getOrderById(order.getId(), user.getId()));
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01ORD04.name());
    }

    @Test
    void listOrdersForAdmin_success() {
        FilterRequest filter = new FilterRequest();
        Pageable pageable = Pageable.unpaged();

        when(pageableFilterConvert.createPageAndSort(filter)).thenReturn(pageable);
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<OrderResponse> page = orderService.listOrdersForAdmin(filter);

        assertThat(page.getContent()).hasSize(1);
    }

    @Test
    void listOrdersForCustomer_success() {
        FilterRequest filter = new FilterRequest();
        Pageable pageable = Pageable.unpaged();

        when(pageableFilterConvert.createPageAndSort(filter)).thenReturn(pageable);
        when(orderRepository.findAllByUserId(eq(user.getId()), any(Pageable.class))).thenReturn(new PageImpl<>(List.of(order)));

        Page<OrderResponse> page = orderService.listOrdersForCustomer(user.getId(), filter);

        assertThat(page.getContent()).hasSize(1);
    }
}

