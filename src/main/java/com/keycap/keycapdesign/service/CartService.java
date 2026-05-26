package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.cart.CartItemRequest;
import com.keycap.keycapdesign.dto.cart.CartItemResponse;
import com.keycap.keycapdesign.entity.CartItem;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.CartItemRepository;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;
import vn.payos.exception.UnauthorizedException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, UserRepository userRepository,
            ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartItemResponse addItem(CartItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        CartItem existing = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
        CartItem item = existing == null ? new CartItem() : existing;
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(request.getQuantity());
        cartItemRepository.save(item);
        return toResponse(item);
    }

    public List<CartItemResponse> listItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void removeItem(Long id, Long userId) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (item.getUser() == null || !item.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Access denied");
        }
        cartItemRepository.delete(item);
    }

    private CartItemResponse toResponse(CartItem item) {
        return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
                item.getQuantity(), item.getProduct().getPrice());
    }
}
