package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.chat.CloseConversationRequest;
import com.keycap.keycapdesign.dto.chat.ConversationCreateRequest;
import com.keycap.keycapdesign.dto.chat.ConversationResponse;
import com.keycap.keycapdesign.dto.chat.MarkReadRequest;
import com.keycap.keycapdesign.dto.chat.MessageResponse;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversationService;
    private final CurrentUserService currentUserService;

    public ConversationController(ConversationService conversationService, CurrentUserService currentUserService) {
        this.conversationService = conversationService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CUSTOMER','STAFF')")
    public ApiResponse<ConversationResponse> create(@Valid @RequestBody ConversationCreateRequest request) {
        User currentUser = currentUserService.getCurrentUser();
        if (currentUser.getRole() == Role.CUSTOMER) {
            request.setCustomerId(currentUser.getId());
        } else if (currentUser.getRole() == Role.STAFF) {
            if (request.getOrderId() == null && request.getTicketId() == null) {
                throw new BadRequestException("orderId or ticketId is required for STAFF to start a conversation");
            }
            request.setStaffId(currentUser.getId());
        }
        return ApiResponse.success(conversationService.createConversation(request, currentUser.getId()));
    }

    @GetMapping
    public ApiResponse<List<ConversationResponse>> list() {
        return ApiResponse.success(conversationService.listConversations(currentUserService.getCurrentUserId()));
    }

    @GetMapping("/{conversationId}/messages")
    public ApiResponse<List<MessageResponse>> messages(@PathVariable Long conversationId) {
        return ApiResponse.success(conversationService.getMessages(conversationId,
                currentUserService.getCurrentUserId()));
    }

    @PutMapping("/{conversationId}/read")
    public ApiResponse<ConversationResponse> markRead(@PathVariable Long conversationId,
            @Valid @RequestBody MarkReadRequest request) {
        request.setUserId(currentUserService.getCurrentUserId());
        return ApiResponse.success(conversationService.markAsRead(conversationId, request));
    }

    @PutMapping("/{conversationId}/close")
    @PreAuthorize("hasRole('STAFF')")
    public ApiResponse<ConversationResponse> close(@PathVariable Long conversationId,
            @Valid @RequestBody CloseConversationRequest request) {
        request.setStaffId(currentUserService.getCurrentUserId());
        return ApiResponse.success(conversationService.closeConversation(conversationId, request));
    }

    @PostMapping("/messages")
    public ApiResponse<MessageResponse> sendMessage(
            @Valid @RequestBody com.keycap.keycapdesign.dto.chat.MessageRequest request) {
        request.setSenderId(currentUserService.getCurrentUserId());
        return ApiResponse.success(conversationService.sendMessage(request));
    }
}
