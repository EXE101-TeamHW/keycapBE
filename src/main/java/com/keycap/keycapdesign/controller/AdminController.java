package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.order.OrderResponse;
import com.keycap.keycapdesign.dto.order.OrderStatusUpdateRequest;
import com.keycap.keycapdesign.dto.product.ProductRequest;
import com.keycap.keycapdesign.dto.product.ProductResponse;
import com.keycap.keycapdesign.dto.user.UserResponse;
import com.keycap.keycapdesign.dto.user.UserStatusUpdateRequest;
import com.keycap.keycapdesign.dto.user.UserRoleUpdateRequest;
import com.keycap.keycapdesign.service.OrderService;
import com.keycap.keycapdesign.service.ProductService;
import com.keycap.keycapdesign.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(UserService userService, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/users")
    public ApiResponse<List<UserResponse>> listUsers() {
        return ApiResponse.success(userService.getAllUsers());
    }

    @PutMapping("/users/{id}/status")
    public ApiResponse<UserResponse> updateUserStatus(@PathVariable Long id,
            @Valid @RequestBody UserStatusUpdateRequest request) {
        return ApiResponse.success(userService.updateStatus(id, request));
    }

    @PutMapping("/users/{id}/role")
    public ApiResponse<UserResponse> updateUserRole(@PathVariable Long id,
            @Valid @RequestBody UserRoleUpdateRequest request) {
        return ApiResponse.success(userService.updateRole(id, request));
    }

    @PostMapping("/products")
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ApiResponse.success(productService.createProduct(request));
    }

    @PutMapping("/products/{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ApiResponse.success(productService.updateProduct(id, request));
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> listOrders() {
        return ApiResponse.success(orderService.listAllOrders());
    }

    @PutMapping("/orders/{id}/status")
    public ApiResponse<OrderResponse> updateOrderStatus(@PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateRequest request) {
        return ApiResponse.success(orderService.updateStatus(id, request));
    }

    @GetMapping("/products")
    public ApiResponse<List<ProductResponse>> listProducts() {
        return ApiResponse.success(productService.listAllProducts());
    }
}
