package com.keycap.keycapdesign.websocket;

import com.keycap.keycapdesign.security.CustomUserDetailsService;
import com.keycap.keycapdesign.security.JwtService;
import com.keycap.keycapdesign.security.UserPrincipal;
import com.keycap.keycapdesign.service.ConversationService;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class WebSocketJwtChannelInterceptor implements ChannelInterceptor {
    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ConversationService conversationService;

    public WebSocketJwtChannelInterceptor(JwtService jwtService, CustomUserDetailsService userDetailsService,
                                          ConversationService conversationService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.conversationService = conversationService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null || accessor.getCommand() == null) {
            return message;
        }
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            accessor.setUser(authenticate(accessor));
        }
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            validateSubscribe(accessor);
        }
        return message;
    }

    private Authentication authenticate(StompHeaderAccessor accessor) {
        String header = accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new BadCredentialsException("Missing WebSocket Authorization header");
        }
        try {
            Claims claims = jwtService.parseClaims(header.substring(7));
            Long userId = Long.parseLong(claims.getSubject());
            UserDetails userDetails = userDetailsService.loadUserById(userId);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid WebSocket token", ex);
        }
    }

    private void validateSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination == null || !destination.startsWith("/topic/chat/")) {
            return;
        }
        Long conversationId = Long.parseLong(destination.substring("/topic/chat/".length()));
        conversationService.validateUserCanAccess(conversationId, authenticatedUserId(accessor.getUser()));
    }

    private Long authenticatedUserId(java.security.Principal principal) {
        if (principal instanceof Authentication authentication
                && authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUser().getId();
        }
        throw new BadCredentialsException("Unauthenticated WebSocket user");
    }
}
