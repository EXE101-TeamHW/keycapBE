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

    public OrderResponse createOrder(OrderCreateRequest request, CartService cartService) {
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
        
        if (request.getType() == com.keycap.keycapdesign.enums.OrderType.CUSTOM) {
            if (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.valueOf(1000000)) < 0) {
                throw new BadRequestException("Custom order minimum deposit amount is 1,000,000 VND");
            }
            total = request.getTotalAmount();
        } else {
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
            
            // Add tax (8%)
            BigDecimal tax = total.multiply(BigDecimal.valueOf(0.08));
            total = total.add(tax);
            
            // Add shipping fee
            if (request.getShippingFee() != null) {
                total = total.add(request.getShippingFee());
            }
            
            // Clear cart items for this user
            try {
                cartService.clearCart(user.getId());
            } catch (Exception e) {
                // Ignore if cart clearing fails
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

    public List<OrderResponse> listOrdersForStaff(Long staffId) {
        return orderRepository.findAll().stream()
                .filter(o -> o.getAssignedStaff() != null && o.getAssignedStaff().getId().equals(staffId))
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

        // Validate state transition
        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (newStatus != OrderStatus.CANCELLED) {
            boolean valid = false;
            switch (currentStatus) {
                case PENDING:
                    if (newStatus == OrderStatus.CONFIRMED) valid = true;
                    break;
                case CONFIRMED:
                    if (newStatus == OrderStatus.PROCESSING) valid = true;
                    break;
                case PROCESSING:
                    if (newStatus == OrderStatus.SHIPPING) valid = true;
                    break;
                case SHIPPING:
                    if (newStatus == OrderStatus.DELIVERED) valid = true;
                    break;
                case DELIVERED:
                    if (newStatus == OrderStatus.COMPLETED) valid = true;
                    break;
                default:
                    valid = false;
            }
            if (!valid && currentStatus != newStatus) {
                throw new BadRequestException("Invalid status transition from " + currentStatus + " to " + newStatus);
            }
        }

        order.setStatus(newStatus);
        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }

        if (request.getProofImages() != null && !request.getProofImages().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                String imagesJson = mapper.writeValueAsString(request.getProofImages());
                // Append or replace? Replace for now, or merge if multiple phases have images.
                // It's safer to read existing and append, but for simplicity we will just append to a list.
                if (order.getProofImagesJson() != null && !order.getProofImagesJson().isEmpty() && !order.getProofImagesJson().equals("[]")) {
                    List<String> existing = mapper.readValue(order.getProofImagesJson(), new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {});
                    existing.addAll(request.getProofImages());
                    order.setProofImagesJson(mapper.writeValueAsString(existing));
                } else {
                    order.setProofImagesJson(imagesJson);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to save proof images: " + e.getMessage(), e);
            }
        }

        orderRepository.save(order);
        return toResponse(order);
    }

    public OrderResponse assignStaff(Long id, Long staffId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        order.setAssignedStaff(staff);
        orderRepository.save(order);
        return toResponse(order);
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.CANCELLED) {
            order.setStatus(OrderStatus.CANCELLED);
            restoreStock(order);
            orderRepository.save(order);
        }
        return toResponse(order);
    }

    public OrderResponse refundOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED && order.getPaymentStatus() == com.keycap.keycapdesign.enums.PaymentStatus.PAID) {
            order.setPaymentStatus(com.keycap.keycapdesign.enums.PaymentStatus.REFUNDED);
            orderRepository.save(order);
        }
        return toResponse(order);
    }

    public void restoreStock(Order order) {
        if (order.getType() == com.keycap.keycapdesign.enums.OrderType.SHOP) {
            List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
            for (OrderItem item : items) {
                Product product = item.getProduct();
                if (product != null && product.getStockQuantity() != null) {
                    product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
                    productRepository.save(product);
                }
            }
        }
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = orderItemRepository.findByOrderId(order.getId()).stream()
                .map(item -> new OrderItemResponse(item.getId(),
                        item.getProduct() == null ? null : item.getProduct().getId(),
                        item.getProduct() == null ? null : item.getProduct().getName(),
                        item.getQuantity(), item.getUnitPrice(), item.getSubtotal()))
                .collect(Collectors.toList());
                
        String customerName = order.getUser().getFullName() != null ? order.getUser().getFullName() : "Customer";
        String customerEmail = order.getUser().getEmail();
        String customerPhone = order.getUser().getPhone();
        String bankAccount = order.getUser().getBankAccount();
        Long staffId = order.getAssignedStaff() != null ? order.getAssignedStaff().getId() : null;
        String staffName = order.getAssignedStaff() != null ? order.getAssignedStaff().getFullName() : null;

        return new OrderResponse(order.getId(), order.getOrderCode(), order.getUser().getId(), 
                customerName, customerEmail, customerPhone, bankAccount, staffId, staffName,
                order.getType(),
                order.getTicket() == null ? null : order.getTicket().getId(), order.getTotalAmount(),
                order.getPaymentMethod(), order.getPaymentStatus(), order.getPaymentType(), order.getShippingAddress(),
                order.getTrackingNumber(), order.getProofImagesJson(), order.getStatus(), order.getCreatedAt(), items);
    }

    private String generateOrderCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "OD-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}



