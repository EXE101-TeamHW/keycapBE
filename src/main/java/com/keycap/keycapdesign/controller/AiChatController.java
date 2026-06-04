package com.keycap.keycapdesign.controller;

import com.keycap.keycapdesign.common.ApiResponse;
import com.keycap.keycapdesign.dto.ai.AiChatRequest;
import com.keycap.keycapdesign.dto.ai.AiChatResponse;
import com.keycap.keycapdesign.security.CurrentUserService;
import com.keycap.keycapdesign.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {
    private final AiChatService aiChatService;
    private final CurrentUserService currentUserService;

    public AiChatController(AiChatService aiChatService, CurrentUserService currentUserService) {
        this.aiChatService = aiChatService;
        this.currentUserService = currentUserService;
    }

    @PostMapping("/chat")
//    @PreAuthorize("hasRole('CUSTOMER')")
    public ApiResponse<AiChatResponse> chat(@Valid @RequestBody AiChatRequest request) {
        Long userId = currentUserService.getCurrentUserId();
        return ApiResponse.success(aiChatService.chat(request, userId));
    }
}
