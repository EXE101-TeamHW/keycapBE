package com.keycap.keycapdesign.dto.ai;

import java.util.List;

public class AiChatResponse {
    private Long conversationId;
    private String reply;
    private List<AiRecommendation> recommendations;
    private List<String> followUpQuestions;
    private boolean aiProviderAvailable;

    public AiChatResponse(Long conversationId, String reply, List<AiRecommendation> recommendations,
            List<String> followUpQuestions, boolean aiProviderAvailable) {
        this.conversationId = conversationId;
        this.reply = reply;
        this.recommendations = recommendations;
        this.followUpQuestions = followUpQuestions;
        this.aiProviderAvailable = aiProviderAvailable;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public String getReply() {
        return reply;
    }

    public List<AiRecommendation> getRecommendations() {
        return recommendations;
    }

    public List<String> getFollowUpQuestions() {
        return followUpQuestions;
    }

    public boolean isAiProviderAvailable() {
        return aiProviderAvailable;
    }
}
