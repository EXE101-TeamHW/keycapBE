package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.order.OrderCreateRequest;
import com.keycap.keycapdesign.dto.order.OrderResponse;
import com.keycap.keycapdesign.dto.order.OrderStatusUpdateRequest;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public OrderController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(orderService.createOrder(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<List<OrderResponse>> list() {
        return ApiResponse.success(orderService.listOrders(currentUserService.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> get(@PathVariable Long id) {
        OrderResponse response = orderService.getOrder(id);
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER
                && !currentUserService.getCurrentUserId().equals(response.getUserId())) {
            throw new UnauthorizedException("Access denied");
        }
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ApiResponse<OrderResponse> updateStatus(@PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ApiResponse.success(orderService.updateStatus(id, request));
    }

    @PutMapping("/{id}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable Long id) {
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER) {
            OrderResponse response = orderService.getOrder(id);
            if (!currentUserService.getCurrentUserId().equals(response.getUserId())) {
                throw new UnauthorizedException("Access denied");
            }
        }
        return ApiResponse.success(orderService.cancelOrder(id));
    }
}
