package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackRequest;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackResponse;
import com.keycap.keycapdesign.dto.mockup.MockupCreateRequest;
import com.keycap.keycapdesign.dto.mockup.MockupResponse;
import com.keycap.keycapdesign.dto.ticket.TicketAssignRequest;
import com.keycap.keycapdesign.dto.ticket.TicketResponse;
import com.keycap.keycapdesign.dto.ticket.TicketStatusUpdateRequest;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.FeedbackService;
import com.keycap.keycapdesign.service.MockupService;
import com.keycap.keycapdesign.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.payos.exception.UnauthorizedException;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final MockupService mockupService;
    private final FeedbackService feedbackService;
    private final CurrentUserService currentUserService;

    public TicketController(TicketService ticketService, MockupService mockupService, FeedbackService feedbackService,
            CurrentUserService currentUserService) {
        this.ticketService = ticketService;
        this.mockupService = mockupService;
        this.feedbackService = feedbackService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> list(@RequestParam(required = false) Long userId) {
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER) {
            return ApiResponse.success(ticketService.listByUser(currentUserService.getCurrentUserId()));
        }
        if (userId != null) {
            return ApiResponse.success(ticketService.listByUser(userId));
        }
        return ApiResponse.success(ticketService.listTickets());
    }

    @GetMapping("/{id}")
    public ApiResponse<TicketResponse> get(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicket(id);
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER
                && !currentUserService.getCurrentUserId().equals(response.getCustomerId())) {
            throw new UnauthorizedException("Access denied");
        }
        return ApiResponse.success(response);
    }

    @PutMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<TicketResponse> assign(@PathVariable Long id, @Valid @RequestBody TicketAssignRequest request) {
        request.setAdminId(currentUserService.getCurrentUserId());
        return ApiResponse.success(ticketService.assignTicket(id, request));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ApiResponse<TicketResponse> updateStatus(@PathVariable Long id,
            @Valid @RequestBody TicketStatusUpdateRequest request) {
        return ApiResponse.success(ticketService.updateStatus(id, request));
    }

    @PostMapping("/{id}/mockups")
    @PreAuthorize("hasAnyRole('STAFF','ADMIN')")
    public ApiResponse<MockupResponse> createMockup(@PathVariable Long id,
            @Valid @RequestBody MockupCreateRequest request) {
        request.setCreatedBy(currentUserService.getCurrentUserId());
        return ApiResponse.success(mockupService.create(id, request));
    }

    @GetMapping("/{id}/mockups")
    public ApiResponse<List<MockupResponse>> listMockups(@PathVariable Long id) {
        if (currentUserService.getCurrentUser().getRole() == Role.CUSTOMER) {
            TicketResponse ticket = ticketService.getTicket(id);
            if (!currentUserService.getCurrentUserId().equals(ticket.getCustomerId())) {
                throw new UnauthorizedException("Access denied");
            }
        }
        return ApiResponse.success(mockupService.listByTicket(id));
    }

    @PostMapping("/{id}/feedback")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<MockupFeedbackResponse> createFeedback(@PathVariable Long id,
            @Valid @RequestBody MockupFeedbackRequest request) {
        TicketResponse ticket = ticketService.getTicket(id);
        if (!currentUserService.getCurrentUserId().equals(ticket.getCustomerId())) {
            throw new UnauthorizedException("Access denied");
        }
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(feedbackService.create(id, request.getMockupId(), request));
    }
}
