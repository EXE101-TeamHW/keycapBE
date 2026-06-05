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
import com.keycap.keycapdesign.enums.PaymentStatus;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.ConversationRepository;
import com.keycap.keycapdesign.repository.OrderItemRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.ProductRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

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
    private final ConversationRepository conversationRepository;
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final TicketService ticketService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        UserRepository userRepository, ProductRepository productRepository,
                        TicketRepository ticketRepository, ConversationRepository conversationRepository,
                        @Lazy ConversationService conversationService,
                        SimpMessagingTemplate messagingTemplate,
                        TicketService ticketService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.ticketRepository = ticketRepository;
        this.conversationRepository = conversationRepository;
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
        this.ticketService = ticketService;
    }

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request, CartService cartService) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Ticket ticket = null;
        if (request.getTicketId() != null) {
            ticket = ticketRepository.findById(request.getTicketId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        }

        if (request.getType() == com.keycap.keycapdesign.enums.OrderType.CUSTOM
                && (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.valueOf(10000)) < 0)) {
            throw new BadRequestException("Tiền cọc tối thiểu cho đơn custom là 10.000đ");
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
        if (request.getType() == com.keycap.keycapdesign.enums.OrderType.SHOP) {
            setOrderDeliveryDeadline(order);
        }

        BigDecimal total = BigDecimal.ZERO;
        
        if (request.getType() == com.keycap.keycapdesign.enums.OrderType.CUSTOM) {
            total = request.getTotalAmount();
            orderRepository.save(order);
        } else {
            orderRepository.save(order);
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
        OrderResponse response = toResponse(order);
        messagingTemplate.convertAndSend("/topic/orders", response);
        return response;
    }

    public List<OrderResponse> listOrders(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> listAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<OrderResponse> listOrdersForStaff(Long staffId) {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(o -> o.getAssignedStaff() != null && o.getAssignedStaff().getId().equals(staffId))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toResponse(order);
    }

    /**
     * Admin: PENDING → CONFIRMED only.
     * Staff: CONFIRMED → PROCESSING → SHIPPING → DELIVERED → COMPLETED.
     */
    public OrderResponse updateStatus(Long id, OrderStatusUpdateRequest request, Role actorRole) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        if (newStatus == OrderStatus.CANCELLED) {
            throw new BadRequestException("Use the cancel endpoint to cancel an order");
        }

        if (actorRole == Role.ADMIN) {
            // Admin can only approve PENDING → CONFIRMED
            if (currentStatus != OrderStatus.PENDING || newStatus != OrderStatus.CONFIRMED) {
                throw new BadRequestException("Admin can only confirm PENDING orders");
            }
        } else if (actorRole == Role.STAFF) {
            // Staff handles CONFIRMED+
            boolean valid = false;
            switch (currentStatus) {
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
            if (!valid) {
                throw new BadRequestException("Invalid status transition from " + currentStatus + " to " + newStatus);
            }
        } else {
            throw new BadRequestException("Unauthorized role for status update");
        }

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.SHIPPING && order.getDeliveryDeadline() == null) {
            setOrderDeliveryDeadline(order);
        }
        if (request.getTrackingNumber() != null) {
            order.setTrackingNumber(request.getTrackingNumber());
        }

        if (request.getProofImages() != null && !request.getProofImages().isEmpty()) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                String imagesJson = mapper.writeValueAsString(request.getProofImages());
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
        OrderResponse response = toResponse(order);
        messagingTemplate.convertAndSend("/topic/orders", response);
        return response;
    }

    /**
     * Admin assigns staff to order, confirms it (PENDING → CONFIRMED), and creates conversation.
     */
    public OrderResponse assignStaffAndConfirm(Long id, Long staffId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new BadRequestException("Cannot assign staff to a cancelled order");
        }
        User staff = userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
        if (staff.getRole() != Role.STAFF) {
            throw new BadRequestException("Assigned user is not a STAFF member");
        }
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order must be PENDING to confirm and assign");
        }

        order.setAssignedStaff(staff);
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        // Auto-create or reuse conversation between customer and assigned staff
        conversationService.getOrCreateConversationForOrder(
                order.getUser().getId(), staffId, order.getId());

        OrderResponse response = toResponse(order);
        messagingTemplate.convertAndSend("/topic/orders", response);
        return response;
    }

    public OrderResponse cancelOrder(Long id, Role actorRole) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() != OrderStatus.CANCELLED) {
            if (actorRole == Role.CUSTOMER && order.getStatus() != OrderStatus.PENDING) {
                throw new BadRequestException("Bạn chỉ có thể hủy đơn hàng khi đơn còn chờ duyệt");
            }
            if (order.getType() == com.keycap.keycapdesign.enums.OrderType.CUSTOM && order.getTicket() != null) {
                com.keycap.keycapdesign.enums.TicketStatus tStatus = order.getTicket().getStatus();
                if (actorRole == Role.CUSTOMER || actorRole == Role.ADMIN) {
                    if (tStatus != com.keycap.keycapdesign.enums.TicketStatus.PENDING && 
                        tStatus != com.keycap.keycapdesign.enums.TicketStatus.IN_REVIEW) {
                        throw new BadRequestException("Không thể hủy đơn hàng custom sau khi bắt đầu thiết kế");
                    }
                } else if (actorRole == Role.STAFF) {
                    if (tStatus == com.keycap.keycapdesign.enums.TicketStatus.CANCELLED ||
                        tStatus == com.keycap.keycapdesign.enums.TicketStatus.COMPLETED) {
                        throw new BadRequestException("Không thể hủy ticket custom ở trạng thái này");
                    }
                }
                order.getTicket().setStatus(com.keycap.keycapdesign.enums.TicketStatus.CANCELLED);
                ticketRepository.save(order.getTicket());
                ticketService.broadcastTicketUpdate(order.getTicket());
            } else {
                if (order.getStatus() != OrderStatus.PENDING && 
                    order.getStatus() != OrderStatus.CONFIRMED && 
                    order.getStatus() != OrderStatus.PROCESSING) {
                    throw new BadRequestException("Can only cancel PENDING, CONFIRMED or PROCESSING orders");
                }
            }
            order.setStatus(OrderStatus.CANCELLED);
            if (isUnpaid(order)) {
                order.setPaymentStatus(PaymentStatus.CANCELLED);
            }
            restoreStock(order);
            orderRepository.save(order);
        } else if (isUnpaid(order)) {
            order.setPaymentStatus(PaymentStatus.CANCELLED);
            orderRepository.save(order);
        }
        OrderResponse response = toResponse(order);
        messagingTemplate.convertAndSend("/topic/orders", response);
        return response;
    }

    public OrderResponse refundOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.CANCELLED && order.getPaymentStatus() == com.keycap.keycapdesign.enums.PaymentStatus.PAID) {
            order.setPaymentStatus(com.keycap.keycapdesign.enums.PaymentStatus.REFUNDED);
            orderRepository.save(order);
        }
        OrderResponse response = toResponse(order);
        messagingTemplate.convertAndSend("/topic/orders", response);
        return response;
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

    private boolean isUnpaid(Order order) {
        return order.getPaymentStatus() != PaymentStatus.PAID
                && order.getPaymentStatus() != PaymentStatus.REFUNDED;
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

        // Find linked conversation
        Long conversationId = conversationRepository.findByOrderId(order.getId())
                .map(c -> c.getId())
                .orElse(null);

        com.keycap.keycapdesign.enums.TicketStatus ticketStatus = order.getTicket() != null ? order.getTicket().getStatus() : null;

        return new OrderResponse(order.getId(), order.getOrderCode(), order.getUser().getId(), 
                customerName, customerEmail, customerPhone, bankAccount, staffId, staffName,
                order.getType(),
                order.getTicket() == null ? null : order.getTicket().getId(), order.getTotalAmount(),
                order.getPaymentMethod(), order.getPaymentStatus(), order.getPaymentType(), order.getShippingAddress(),
                order.getTrackingNumber(), order.getProofImagesJson(), order.getStatus(), order.getCreatedAt(), items,
                conversationId, ticketStatus, order.getDeliveryDeadline());
    }

    private void setOrderDeliveryDeadline(Order order) {
        String address = order.getShippingAddress();
        if (address != null) {
            String addrLower = address.toLowerCase();
            if (addrLower.contains("hồ chí minh") || addrLower.contains("ho chi minh") || addrLower.contains("hcm")) {
                order.setDeliveryDeadline(LocalDate.now().plusDays(3));
            } else {
                order.setDeliveryDeadline(LocalDate.now().plusDays(7));
            }
        }
    }

    private String generateOrderCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "OD-" + date + "-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
