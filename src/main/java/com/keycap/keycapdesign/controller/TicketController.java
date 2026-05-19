package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackRequest;
import com.keycap.keycapdesign.dto.feedback.MockupFeedbackResponse;
import com.keycap.keycapdesign.dto.mockup.MockupCreateRequest;
import com.keycap.keycapdesign.dto.mockup.MockupResponse;
import com.keycap.keycapdesign.dto.ticket.TicketAssignRequest;
import com.keycap.keycapdesign.dto.ticket.TicketResponse;
import com.keycap.keycapdesign.dto.ticket.TicketStatusUpdateRequest;
import com.keycap.keycapdesign.service.FeedbackService;
import com.keycap.keycapdesign.service.MockupService;
import com.keycap.keycapdesign.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final MockupService mockupService;
    private final FeedbackService feedbackService;

    public TicketController(TicketService ticketService, MockupService mockupService, FeedbackService feedbackService) {
        this.ticketService = ticketService;
        this.mockupService = mockupService;
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> list() {
        return ApiResponse.success(ticketService.listTickets());
    }

    @GetMapping("/{id}")
    public ApiResponse<TicketResponse> get(@PathVariable Long id) {
        return ApiResponse.success(ticketService.getTicket(id));
    }

    @PutMapping("/{id}/assign")
    public ApiResponse<TicketResponse> assign(@PathVariable Long id, @Valid @RequestBody TicketAssignRequest request) {
        return ApiResponse.success(ticketService.assignTicket(id, request));
    }

    @PutMapping("/{id}/status")
    public ApiResponse<TicketResponse> updateStatus(@PathVariable Long id,
                                                    @Valid @RequestBody TicketStatusUpdateRequest request) {
        return ApiResponse.success(ticketService.updateStatus(id, request));
    }

    @PostMapping("/{id}/mockups")
    public ApiResponse<MockupResponse> createMockup(@PathVariable Long id,
                                                    @Valid @RequestBody MockupCreateRequest request) {
        return ApiResponse.success(mockupService.create(id, request));
    }

    @GetMapping("/{id}/mockups")
    public ApiResponse<List<MockupResponse>> listMockups(@PathVariable Long id) {
        return ApiResponse.success(mockupService.listByTicket(id));
    }

    @PostMapping("/{id}/feedback")
    public ApiResponse<MockupFeedbackResponse> createFeedback(@PathVariable Long id,
                                                              @Valid @RequestBody MockupFeedbackRequest request) {
        return ApiResponse.success(feedbackService.create(id, request.getMockupId(), request));
    }
}



