package com.keycap.keycapdesign.dto.chat;

import com.keycap.keycapdesign.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MessageRequest {
    @NotNull
    private Long conversationId;

    @NotNull
    private Long senderId;

    @NotBlank
    private String content;

    private MessageType messageType = MessageType.TEXT;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
