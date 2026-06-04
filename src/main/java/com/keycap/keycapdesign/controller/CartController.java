package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.cart.CartItemRequest;
import com.keycap.keycapdesign.dto.cart.CartItemResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.CartService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {
    private final CartService cartService;
    private final CurrentUserService currentUserService;

    public CartController(CartService cartService, CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/items")
    public ApiResponse<CartItemResponse> addItem(@Valid @RequestBody CartItemRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(cartService.addItem(request));
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> list() {
        return ApiResponse.success(cartService.listItems(currentUserService.getCurrentUserId()));
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        cartService.removeItem(id, currentUserService.getCurrentUserId());
        return ApiResponse.success(null);
    }

    @PutMapping("/items/{id}")
    public ApiResponse<CartItemResponse> updateQuantity(@PathVariable Long id, @RequestBody CartItemRequest request) {
        return ApiResponse.success(cartService.updateQuantity(id, currentUserService.getCurrentUserId(), request.getQuantity()));
    }
}
