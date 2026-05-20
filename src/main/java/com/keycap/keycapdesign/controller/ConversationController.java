package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.chat.CloseConversationRequest;
import com.keycap.keycapdesign.dto.chat.ConversationCreateRequest;
import com.keycap.keycapdesign.dto.chat.ConversationResponse;
import com.keycap.keycapdesign.dto.chat.MarkReadRequest;
import com.keycap.keycapdesign.dto.chat.MessageResponse;
import com.keycap.keycapdesign.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ApiResponse<ConversationResponse> create(@Valid @RequestBody ConversationCreateRequest request) {
        return ApiResponse.success(conversationService.createConversation(request));
    }

    @GetMapping
    public ApiResponse<List<ConversationResponse>> list(@RequestParam Long userId) {
        return ApiResponse.success(conversationService.listConversations(userId));
    }

    @GetMapping("/{conversationId}/messages")
    public ApiResponse<List<MessageResponse>> messages(@PathVariable Long conversationId,
                                                       @RequestParam Long userId) {
        return ApiResponse.success(conversationService.getMessages(conversationId, userId));
    }

    @PutMapping("/{conversationId}/read")
    public ApiResponse<ConversationResponse> markRead(@PathVariable Long conversationId,
                                                      @Valid @RequestBody MarkReadRequest request) {
        return ApiResponse.success(conversationService.markAsRead(conversationId, request));
    }

    @PutMapping("/{conversationId}/close")
    public ApiResponse<ConversationResponse> close(@PathVariable Long conversationId,
                                                   @Valid @RequestBody CloseConversationRequest request) {
        return ApiResponse.success(conversationService.closeConversation(conversationId, request));
    }
}
