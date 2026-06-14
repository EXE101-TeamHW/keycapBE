package com.keycap.keycapdesign.repository;

import com.keycap.keycapdesign.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByCreatedAtAsc(Long conversationId);
    long countByConversationIdAndSenderIdNotAndIsReadFalse(Long conversationId, Long senderId);

    @Query("""
            select m.conversation.id, count(m)
            from Message m
            where m.conversation.id in :conversationIds
              and m.sender.id <> :viewerId
              and m.isRead = false
            group by m.conversation.id
            """)
    List<Object[]> countUnreadByConversationIds(@Param("conversationIds") List<Long> conversationIds,
                                                @Param("viewerId") Long viewerId);
}
