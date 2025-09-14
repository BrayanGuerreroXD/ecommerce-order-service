package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateOrderRequest;
import com.test.ecommerceorderservice.application.dto.response.OrderResponse;
import com.test.ecommerceorderservice.application.event.OrderStatusChangedEvent;
import com.test.ecommerceorderservice.application.mapper.OrderMapper;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderApplicationService {

    private final OrderRepository orderRepository;
    private final PageableFilterConvert pageableFilterConvert;
    private final DomainEventPublisher eventPublisher;

    private final ProductApplicationService productApplicationService;
    private final UserApplicationService userApplicationService;
    private final InventoryApplicationService inventoryService;
    private final OrderLogApplicationService orderLogApplicationService;

    public static final String CACHE = "order";

    @Transactional
    @CachePut(value = CACHE, key = "#result.id")
    public OrderResponse createOrder(CreateOrderRequest request, Long userId) {
        log.info("Creating order for userId {} productId {} qty {}", userId, request.getProductId(), request.getQuantity());

        User user = userApplicationService.getUserModelById(userId);
        Product product = productApplicationService.getProductModelById(request.getProductId());

        // Try atomic reservation on inventory (fast path). Returns true if reserved.
        boolean reserved = inventoryService.decreaseForOrder(product.getId(), request.getQuantity());
        if (!reserved) {
            throw new ConflictException(ExceptionCodeEnum.C01ORD03); // out of stock code (add to your enum)
        }

        Double price = product.getPrice() * request.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(request.getQuantity());
        order.setTotalPrice(price);
        order.setStatus(OrderStatus.CREATED);

        Order saved = orderRepository.save(order);

        // Save order log
        orderLogApplicationService.saveOrderLog(saved);

        // Publish event asynchronously
        eventPublisher.publish(new OrderStatusChangedEvent(
                saved.getId(),
                saved.getStatus().name(),
                saved
        ));

        return OrderMapper.toResponse(saved);
    }

    @Transactional
    @CachePut(value = CACHE, key = "#result.id")
    public OrderResponse payOrder(Long orderId, Long userId) {
        Order order = getOrderValidated(orderId, userId);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException(ExceptionCodeEnum.C01ORD02);
        }

        order.setStatus(OrderStatus.PAID);
        Order saved = orderRepository.save(order);

        // Save order log
        orderLogApplicationService.saveOrderLog(saved);

        eventPublisher.publish(new OrderStatusChangedEvent(
                saved.getId(),
                saved.getStatus().name(),
                saved
        ));

        return OrderMapper.toResponse(saved);
    }

    @Transactional
    @CachePut(value = CACHE, key = "#result.id")
    public OrderResponse cancelOrder(Long orderId, Long userId) {
        Order order = getOrderValidated(orderId, userId);

        if (order.getStatus() == OrderStatus.PAID) {
            throw new BadRequestException(ExceptionCodeEnum.C01ORD05);
        }

        order.setStatus(OrderStatus.CANCELED);
        Order saved = orderRepository.save(order);

        // Save order log
        orderLogApplicationService.saveOrderLog(saved);

        // release inventory (increase)
        inventoryService.increaseByProduct(order.getProduct().getId(), order.getQuantity());

        eventPublisher.publish(new OrderStatusChangedEvent(
                saved.getId(),
                saved.getStatus().name(),
                saved
        ));

        return OrderMapper.toResponse(saved);
    }

    @Cacheable(value = CACHE, key = "#orderId")
    public OrderResponse getOrderById(Long orderId, Long userId) {
        Order order = getOrderValidated(orderId, userId);
        OrderResponse response = OrderMapper.toResponse(order);
        response.setLogs(orderLogApplicationService.findAllOrderLogResponseByOrderId(orderId));
        return response;
    }

    @Cacheable(value = CACHE, key = "#orderId")
    public OrderResponse getOrderByIdForAdmin(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionCodeEnum.C01ORD01));
        OrderResponse response = OrderMapper.toResponse(order);
        response.setLogs(orderLogApplicationService.findAllOrderLogResponseByOrderId(orderId));
        return response;
    }

    private Order getOrderValidated(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BadRequestException(ExceptionCodeEnum.C01ORD01));

        if (!order.getUser().getId().equals(userId)) {
            throw new BadRequestException(ExceptionCodeEnum.C01ORD04);
        }
        return order;
    }

    public Page<OrderResponse> listOrdersForAdmin(FilterRequest filterRequest) {
        Pageable pageable = pageableFilterConvert.createPageAndSort(filterRequest);
        return orderRepository.findAll(pageable)
                .map(OrderMapper::toResponse);
    }

    public Page<OrderResponse> listOrdersForCustomer(Long userId, FilterRequest filterRequest) {
        Pageable pageable = pageableFilterConvert.createPageAndSort(filterRequest);
        return orderRepository.findAllByUserId(userId, pageable)
                .map(OrderMapper::toResponse);
    }
}