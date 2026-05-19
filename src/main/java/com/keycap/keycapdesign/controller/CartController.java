package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.cart.CartItemRequest;
import com.keycap.keycapdesign.dto.cart.CartItemResponse;
import com.keycap.keycapdesign.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ApiResponse<CartItemResponse> addItem(@Valid @RequestBody CartItemRequest request) {
        return ApiResponse.success(cartService.addItem(request));
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> list(@RequestParam Long userId) {
        return ApiResponse.success(cartService.listItems(userId));
    }

    @DeleteMapping("/items/{id}")
    public ApiResponse<Object> delete(@PathVariable Long id) {
        cartService.removeItem(id);
        return ApiResponse.success(null);
    }
}

