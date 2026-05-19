package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.order.OrderCreateRequest;
import com.keycap.keycapdesign.dto.order.OrderItemRequest;
import com.keycap.keycapdesign.dto.order.OrderItemResponse;
import com.keycap.keycapdesign.dto.order.OrderResponse;
import com.keycap.keycapdesign.dto.order.OrderStatusUpdateRequest;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.entity.OrderItem;
import com.keycap.keycapdesign.entity.Product;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.OrderStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.OrderItemRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final TicketRepository ticketRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        UserRepository userRepository, ProductRepository productRepository,
                        TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
    }

    public OrderResponse createOrder(OrderCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Ticket ticket = null;
        if (request.getTicketId() != null) {
            ticket = ticketRepository.findById(request.getTicketId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        }

        Order order = new Order();
        order.setOrderCode(generateOrderCode());
        order.setUser(user);
        order.setType(request.getType());
        order.setTicket(ticket);
        order.setPaymentMethod(request.getPaymentMethod());
        order.setPaymentType(request.getPaymentType());
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(OrderStatus.PENDING);
        orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : request.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            if (product.getStockQuantity() != null && product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product " + product.getName());
            }
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(itemRequest.getQuantity());
            item.setUnitPrice(product.getPrice());
            item.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            orderItemRepository.save(item);
            total = total.add(item.getSubtotal());
            if (product.getStockQuantity() != null) {
                product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
                productRepository.save(product);
            }
        }
        order.setTotalAmount(total);
        orderRepository.save(order);
        return toResponse(order);
    }

    public List<OrderResponse> listOrders(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> listAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toResponse(order);
    }

    public OrderResponse updateStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(request.getStatus());
        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }
        orderRepository.save(order);
        return toResponse(order);
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = orderItemRepository.findByOrderId(order.getId()).stream()
                .map(item -> new OrderItemResponse(item.getId(),
                        item.getProduct() == null ? null : item.getProduct().getId(),
                        item.getProduct() == null ? null : item.getProduct().getName(),
                        item.getQuantity(), item.getUnitPrice(), item.getSubtotal()))
                .collect(Collectors.toList());
        return new OrderResponse(order.getId(), order.getOrderCode(), order.getUser().getId(), order.getType(),
                order.getTicket() == null ? null : order.getTicket().getId(), order.getTotalAmount(),
                order.getPaymentMethod(), order.getPaymentStatus(), order.getPaymentType(), order.getShippingAddress(),
                order.getTrackingNumber(), order.getStatus(), order.getCreatedAt(), items);
    }

    private String generateOrderCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "OD-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}



