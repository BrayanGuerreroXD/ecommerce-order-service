package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.CreateOrderRequest;
import com.test.ecommerceorderservice.application.dto.response.OrderResponse;
import com.test.ecommerceorderservice.application.service.OrderApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.Role;
import com.test.ecommerceorderservice.infrastructure.security.service.HandleCurrentSessionUserIdService;
import com.test.ecommerceorderservice.infrastructure.web.dto.DefaultResponse;
import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderService;
    private final HandleCurrentSessionUserIdService handleCurrentSessionUserIdService;

    @PostMapping
    @RoleVerify(Role.CUSTOMER)
    public DefaultResponse<OrderResponse> create(@RequestBody @Valid CreateOrderRequest req, HttpServletRequest tokenRequest) {
        Long userId = handleCurrentSessionUserIdService.handleCurrentSessionUserId(tokenRequest);
        return new DefaultResponse<>(orderService.createOrder(req, userId));
    }

    @PostMapping("/{orderId}/pay")
    @RoleVerify(Role.CUSTOMER)
    public DefaultResponse<OrderResponse> pay(@PathVariable Long orderId, HttpServletRequest tokenRequest) {
        Long userId = handleCurrentSessionUserIdService.handleCurrentSessionUserId(tokenRequest);
        return new DefaultResponse<>(orderService.payOrder(orderId, userId));
    }

    @PostMapping("/{orderId}/cancel")
    @RoleVerify(Role.CUSTOMER)
    public DefaultResponse<OrderResponse> cancel(@PathVariable Long orderId, HttpServletRequest tokenRequest) {
        Long userId = handleCurrentSessionUserIdService.handleCurrentSessionUserId(tokenRequest);
        return new DefaultResponse<>(orderService.cancelOrder(orderId, userId));
    }

    @GetMapping("find-all-paginated")
    @RoleVerify(Role.ADMIN)
    public DefaultResponse<Page<OrderResponse>> list(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "column", required = false) String column,
            @RequestParam(name = "order", required = false) String order)
    {
        FilterRequest filterRequest = new FilterRequest(page, size, column, order);
        return new DefaultResponse<>(orderService.listOrdersForAdmin(filterRequest));
    }

    @GetMapping("find-all-by-user-paginated")
    @RoleVerify(Role.CUSTOMER)
    public DefaultResponse<Page<OrderResponse>> listByUserId(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "column", required = false) String column,
            @RequestParam(name = "order", required = false) String order,
            HttpServletRequest tokenRequest
    ) {
        Long userId = handleCurrentSessionUserIdService.handleCurrentSessionUserId(tokenRequest);
        FilterRequest filterRequest = new FilterRequest(page, size, column, order);
        return new DefaultResponse<>(orderService.listOrdersForCustomer(userId, filterRequest));
    }

    @GetMapping("/admin/{orderId}")
    @RoleVerify(Role.ADMIN)
    public DefaultResponse<OrderResponse> getByIdForAdmin(@PathVariable Long orderId) {
        return new DefaultResponse<>(orderService.getOrderByIdForAdmin(orderId));
    }

    @GetMapping("/{orderId}")
    @RoleVerify(Role.CUSTOMER)
    public DefaultResponse<OrderResponse> getById(@PathVariable Long orderId, HttpServletRequest tokenRequest) {
        Long userId = handleCurrentSessionUserIdService.handleCurrentSessionUserId(tokenRequest);
        return new DefaultResponse<>(orderService.getOrderById(orderId, userId));
    }

}