package com.keycap.keycapdesign.websocket;

import com.keycap.keycapdesign.dto.chat.MessageRequest;
import com.keycap.keycapdesign.dto.chat.MessageResponse;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.security.UserPrincipal;
import com.keycap.keycapdesign.service.ConversationService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {
    private final ConversationService conversationService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ConversationService conversationService, SimpMessagingTemplate messagingTemplate) {
        this.conversationService = conversationService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat")
    public void chat(@Payload MessageRequest request, Principal principal) {
        Long userId = authenticatedUserId(principal);
        if (!userId.equals(request.getSenderId())) {
            throw new BadRequestException("senderId does not match WebSocket token");
        }

        MessageResponse response = conversationService.sendMessage(request);
        messagingTemplate.convertAndSend("/topic/chat/" + response.getConversationId(), response);
    }

    private Long authenticatedUserId(Principal principal) {
        if (principal instanceof Authentication authentication
                && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser().getId();
        }
        throw new BadRequestException("Unauthenticated WebSocket user");
    }
}
