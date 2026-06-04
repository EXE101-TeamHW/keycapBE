package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.cart.CartItemRequest;
import com.keycap.keycapdesign.dto.cart.CartItemResponse;
import com.keycap.keycapdesign.entity.CartItem;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.exception.BadRequestException;
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
        int quantityToAdd = request.getQuantity() == null ? 1 : request.getQuantity();
        if (quantityToAdd <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        CartItem existing = cartItemRepository.findByUserIdAndProductId(user.getId(), product.getId());
        CartItem item = existing == null ? new CartItem() : existing;
        int currentQuantity = existing == null || existing.getQuantity() == null ? 0 : existing.getQuantity();
        int nextQuantity = currentQuantity + quantityToAdd;
        int stockQuantity = product.getStockQuantity() == null ? 0 : product.getStockQuantity();
        if (nextQuantity > stockQuantity) {
            throw new BadRequestException("Not enough stock for this product");
        }
        item.setUser(user);
        item.setProduct(product);
        item.setQuantity(nextQuantity);
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

    public CartItemResponse updateQuantity(Long id, Long userId, Integer quantity) {
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        if (item.getUser() == null || !item.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Access denied");
        }
        if (quantity == null || quantity <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }
        int stockQuantity = item.getProduct().getStockQuantity() == null ? 0 : item.getProduct().getStockQuantity();
        if (quantity > stockQuantity) {
            throw new BadRequestException("Not enough stock for this product");
        }
        item.setQuantity(quantity);
        cartItemRepository.save(item);
        return toResponse(item);
    }

    private CartItemResponse toResponse(CartItem item) {
        return new CartItemResponse(item.getId(), item.getProduct().getId(), item.getProduct().getName(),
                item.getQuantity(), item.getProduct().getPrice());
    }

    public void clearCart(Long userId) {
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(items);
    }
}
