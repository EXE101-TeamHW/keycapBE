package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.custom.CustomRequestCreateRequest;
import com.keycap.keycapdesign.dto.custom.CustomRequestResponse;
import com.keycap.keycapdesign.dto.custom.CustomCheckoutRequest;
import com.keycap.keycapdesign.dto.order.OrderCreateRequest;
import com.keycap.keycapdesign.dto.order.OrderResponse;
import com.keycap.keycapdesign.dto.payment.PayOsCreatePaymentRequest;
import com.keycap.keycapdesign.dto.payment.PaymentUrlResponse;
import com.keycap.keycapdesign.entity.CustomRequest;
import com.keycap.keycapdesign.entity.Ticket;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.OrderType;
import com.keycap.keycapdesign.enums.PaymentMethod;
import com.keycap.keycapdesign.enums.TicketStatus;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.CustomRequestRepository;
import com.keycap.keycapdesign.repository.TicketRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import com.keycap.keycapdesign.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomRequestService {
    private final CustomRequestRepository customRequestRepository;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final OrderService orderService;
    private final PayOsService payOsService;

    public CustomRequestService(CustomRequestRepository customRequestRepository, UserRepository userRepository,
                                TicketRepository ticketRepository, OrderService orderService, PayOsService payOsService) {
        this.customRequestRepository = customRequestRepository;
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
        this.orderService = orderService;
        this.payOsService = payOsService;
    }

    @Transactional
    public CustomRequestResponse create(CustomRequestCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String designName = request.getDesignName() == null ? "" : request.getDesignName().trim();
        if (designName.isEmpty()) {
            throw new BadRequestException("Tên dự án/thiết kế không được để trống");
        }
        if (customRequestRepository.existsByUserIdAndDesignNameIgnoreCaseAndStatusNot(user.getId(), designName, TicketStatus.CANCELLED)) {
            throw new BadRequestException("Tên dự án/thiết kế đã tồn tại. Vui lòng chọn tên khác.");
        }

        CustomRequest customRequest = new CustomRequest();
        customRequest.setUser(user);
        customRequest.setDesignName(designName);
        customRequest.setLayout(request.getLayout());
        customRequest.setTheme(request.getTheme());
        customRequest.setReferenceImagesJson(JsonUtil.toJson(request.getReferenceImages()));
        customRequest.setNotes(request.getNotes());
        customRequest.setStatus(TicketStatus.PENDING);
        customRequestRepository.save(customRequest);

        Ticket ticket = new Ticket();
        ticket.setRequest(customRequest);
        ticket.setTicketCode(generateTicketCode());
        ticket.setStatus(TicketStatus.PENDING);
        TicketService.setInitialDeadline(ticket, customRequest.getNotes());
        ticketRepository.save(ticket);

        return toResponse(customRequest, ticket.getId());
    }

    @Transactional
    public PaymentUrlResponse createCheckout(CustomCheckoutRequest request) {
        if (request.getTotalAmount() == null || request.getTotalAmount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw new BadRequestException("Tiền cọc tối thiểu cho đơn custom là 10.000đ");
        }

        CustomRequestCreateRequest customRequest = new CustomRequestCreateRequest();
        customRequest.setUserId(request.getUserId());
        customRequest.setDesignName(request.getDesignName());
        customRequest.setLayout(request.getLayout());
        customRequest.setTheme(request.getTheme());
        customRequest.setReferenceImages(request.getReferenceImages());
        customRequest.setNotes(request.getNotes());
        CustomRequestResponse customResponse = create(customRequest);

        OrderCreateRequest orderRequest = new OrderCreateRequest();
        orderRequest.setUserId(request.getUserId());
        orderRequest.setType(OrderType.CUSTOM);
        orderRequest.setTicketId(customResponse.getTicketId());
        orderRequest.setTotalAmount(request.getTotalAmount());
        orderRequest.setShippingAddress(request.getShippingAddress());
        orderRequest.setPaymentMethod(PaymentMethod.PAYOS);
        orderRequest.setItems(Collections.emptyList());
        OrderResponse orderResponse = orderService.createOrder(orderRequest, null);

        PayOsCreatePaymentRequest paymentRequest = new PayOsCreatePaymentRequest();
        paymentRequest.setOrderId(orderResponse.getId());
        return payOsService.createPaymentUrl(paymentRequest);
    }

    public List<CustomRequestResponse> listByUser(Long userId) {
        return customRequestRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(req -> {
                    Ticket ticket = ticketRepository.findByRequestId(req.getId()).orElse(null);
                    return toResponse(req, ticket != null ? ticket.getId() : null);
                })
                .collect(Collectors.toList());
    }

    public CustomRequestResponse getById(Long id) {
        CustomRequest customRequest = customRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Custom request not found"));
        Ticket ticket = ticketRepository.findByRequestId(customRequest.getId()).orElse(null);
        return toResponse(customRequest, ticket != null ? ticket.getId() : null);
    }

    private CustomRequestResponse toResponse(CustomRequest customRequest, Long ticketId) {
        String customerName = customRequest.getUser().getFullName() != null ? customRequest.getUser().getFullName() : "Customer";
        String customerEmail = customRequest.getUser().getEmail();
        String customerPhone = customRequest.getUser().getPhone();
        
        return new CustomRequestResponse(customRequest.getId(), ticketId, customRequest.getUser().getId(),
                customerName, customerEmail, customerPhone,
                customRequest.getDesignName(), customRequest.getLayout(), customRequest.getTheme(),
                JsonUtil.fromJson(customRequest.getReferenceImagesJson()), customRequest.getNotes(),
                customRequest.getStatus(), customRequest.getCreatedAt());
    }

    private String generateTicketCode() {
        String date = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        return "KC-" + date + "-" + UUID.randomUUID().toString().substring(0, 3).toUpperCase();
    }
}

