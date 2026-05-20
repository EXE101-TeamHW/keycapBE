package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    long countByConversationIdAndSenderIdNotAndIsReadFalse(Long conversationId, Long senderId);
}
