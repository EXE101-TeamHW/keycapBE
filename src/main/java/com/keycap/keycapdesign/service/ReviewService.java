package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.review.ReviewRequest;
import com.keycap.keycapdesign.dto.review.ReviewResponse;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.entity.Review;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.repository.ReviewRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository,
                         UserRepository userRepository, OrderRepository orderRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public ReviewResponse createReview(Long productId, Long orderId, ReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Order order = null;
        if (orderId != null) {
            order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            
            if (!order.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("You can only review products from your own orders");
            }
            if (reviewRepository.existsByOrderIdAndProductId(orderId, productId)) {
                throw new BadRequestException("You have already reviewed this product for this order");
            }
            if (!com.keycap.keycapdesign.enums.OrderStatus.DELIVERED.equals(order.getStatus())
                    && !com.keycap.keycapdesign.enums.OrderStatus.COMPLETED.equals(order.getStatus())) {
                throw new IllegalArgumentException("You can only review products from delivered or completed orders");
            }
            boolean containsProduct = order.getItems().stream()
                    .anyMatch(item -> item.getProduct() != null && item.getProduct().getId().equals(productId));
            if (!containsProduct) {
                throw new IllegalArgumentException("This order does not contain the specified product");
            }
        }
        
        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setOrder(order);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        reviewRepository.save(review);
        return toResponse(review);
    }

    public List<ReviewResponse> listByProduct(Long productId) {
        return reviewRepository.findByProductId(productId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ReviewResponse> listAll() {
        return reviewRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public long countReviews() {
        return reviewRepository.count();
    }

    public void deleteReviewAsAdmin(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }

    public ReviewResponse updateReview(Long productId, Long reviewId, ReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (review.getUser() == null || request.getUserId() == null
                || !review.getUser().getId().equals(request.getUserId())) {
            throw new BadRequestException("You can only update your own review");
        }

        if (review.getProduct() == null || !review.getProduct().getId().equals(productId)) {
            throw new BadRequestException("Review does not belong to this product");
        }

        if (request.getRating() == null && request.getComment() == null) {
            throw new BadRequestException("No review fields to update");
        }

        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        if (request.getComment() != null) {
            review.setComment(request.getComment());
        }

        reviewRepository.save(review);
        return toResponse(review);
    }

    public void deleteReview(Long productId, Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (review.getUser() == null || userId == null || !review.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only delete your own review");
        }

        if (review.getProduct() == null || !review.getProduct().getId().equals(productId)) {
            throw new BadRequestException("Review does not belong to this product");
        }

        reviewRepository.delete(review);
    }

    private ReviewResponse toResponse(Review review) {
        String userName = review.getUser() != null ? 
            (review.getUser().getFullName() != null ? review.getUser().getFullName() : review.getUser().getEmail()) : "Unknown User";

        String productName = null;
        String productImage = null;
        if (review.getProduct() != null) {
            productName = review.getProduct().getName();
            List<String> images = JsonUtil.fromJson(review.getProduct().getImagesJson());
            if (images != null && !images.isEmpty()) {
                productImage = images.get(0);
            }
        }

        return new ReviewResponse(review.getId(),
                review.getOrder() == null ? null : review.getOrder().getId(),
                review.getProduct() == null ? null : review.getProduct().getId(),
                review.getUser() == null ? null : review.getUser().getId(),
                userName,
                review.getRating(), 
                review.getComment(), 
                review.getCreatedAt(),
                productName,
                productImage);
    }
}

