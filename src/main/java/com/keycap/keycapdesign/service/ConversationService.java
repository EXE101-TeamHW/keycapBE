package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.chat.CloseConversationRequest;
import com.keycap.keycapdesign.dto.chat.ConversationCreateRequest;
import com.keycap.keycapdesign.dto.chat.ConversationResponse;
import com.keycap.keycapdesign.dto.chat.MarkReadRequest;
import com.keycap.keycapdesign.dto.chat.MessageRequest;
import com.keycap.keycapdesign.dto.chat.MessageResponse;
import com.keycap.keycapdesign.entity.Conversation;
import com.keycap.keycapdesign.entity.Message;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.ConversationStatus;
import com.keycap.keycapdesign.enums.MessageType;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.ConversationRepository;
import com.keycap.keycapdesign.repository.MessageRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository,
                               UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ConversationResponse createConversation(ConversationCreateRequest request) {
        User customer = getUser(request.getCustomerId());
        if (customer.getRole() != Role.CUSTOMER) {
            throw new BadRequestException("Only CUSTOMER can start a conversation");
        }

        User staff = null;
        if (request.getStaffId() != null) {
            staff = getUser(request.getStaffId());
            if (staff.getRole() != Role.STAFF) {
                throw new BadRequestException("staffId must belong to STAFF");
            }
        }

        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setStaff(staff);
        conversation.setStatus(ConversationStatus.OPEN);
        conversationRepository.save(conversation);
        return toConversationResponse(conversation, customer.getId());
    }

    public List<ConversationResponse> listConversations(Long userId) {
        User user = getUser(userId);
        List<Conversation> conversations;
        if (user.getRole() == Role.CUSTOMER) {
            conversations = conversationRepository.findByCustomerIdOrderByCreatedAtDesc(userId);
        } else if (user.getRole() == Role.STAFF) {
            conversations = conversationRepository.findAllByOrderByCreatedAtDesc();
        } else {
            conversations = conversationRepository.findAllByOrderByCreatedAtDesc();
        }
        return conversations.stream()
                .map(conversation -> toConversationResponse(conversation, userId))
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getMessages(Long conversationId, Long userId) {
        Conversation conversation = getConversation(conversationId);
        User user = getUser(userId);
        validateParticipant(conversation, user);
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).stream()
                .map(this::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageResponse sendMessage(MessageRequest request) {
        Conversation conversation = getConversation(request.getConversationId());
        if (conversation.getStatus() == ConversationStatus.CLOSED) {
            throw new BadRequestException("Conversation is closed");
        }

        User sender = getUser(request.getSenderId());
        validateParticipant(conversation, sender);
        assignStaffIfNeeded(conversation, sender);

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setMessageType(request.getMessageType() == null ? MessageType.TEXT : request.getMessageType());
        message.setIsRead(false);
        messageRepository.save(message);
        return toMessageResponse(message);
    }

    @Transactional
    public ConversationResponse markAsRead(Long conversationId, MarkReadRequest request) {
        Conversation conversation = getConversation(conversationId);
        User user = getUser(request.getUserId());
        validateParticipant(conversation, user);

        List<Message> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
        for (Message message : messages) {
            if (!message.getSender().getId().equals(user.getId())) {
                message.setIsRead(true);
            }
        }
        messageRepository.saveAll(messages);
        return toConversationResponse(conversation, user.getId());
    }

    @Transactional
    public ConversationResponse closeConversation(Long conversationId, CloseConversationRequest request) {
        Conversation conversation = getConversation(conversationId);
        User staff = getUser(request.getStaffId());
        if (staff.getRole() != Role.STAFF) {
            throw new BadRequestException("Only STAFF can close a conversation");
        }
        validateParticipant(conversation, staff);
        assignStaffIfNeeded(conversation, staff);
        conversation.setStatus(ConversationStatus.CLOSED);
        conversationRepository.save(conversation);
        return toConversationResponse(conversation, staff.getId());
    }

    public Conversation getConversation(Long conversationId) {
        return conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
    }

    public void validateUserCanAccess(Long conversationId, Long userId) {
        Conversation conversation = getConversation(conversationId);
        User user = getUser(userId);
        validateParticipant(conversation, user);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private void validateParticipant(Conversation conversation, User user) {
        if (user.getRole() == Role.CUSTOMER && conversation.getCustomer().getId().equals(user.getId())) {
            return;
        }
        if (user.getRole() == Role.STAFF) {
            Long staffId = conversation.getStaff() == null ? null : conversation.getStaff().getId();
            if (staffId == null || staffId.equals(user.getId())) {
                return;
            }
        }
        throw new BadRequestException("User is not allowed in this conversation");
    }

    private void assignStaffIfNeeded(Conversation conversation, User user) {
        if (user.getRole() == Role.STAFF && conversation.getStaff() == null) {
            conversation.setStaff(user);
            conversationRepository.save(conversation);
        }
    }

    private ConversationResponse toConversationResponse(Conversation conversation, Long viewerId) {
        long unreadCount = messageRepository.countByConversationIdAndSenderIdNotAndIsReadFalse(
                conversation.getId(), viewerId);
        User staff = conversation.getStaff();
        return new ConversationResponse(conversation.getId(),
                conversation.getCustomer().getId(),
                conversation.getCustomer().getFullName(),
                staff == null ? null : staff.getId(),
                staff == null ? null : staff.getFullName(),
                conversation.getStatus(),
                unreadCount,
                conversation.getCreatedAt());
    }

    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getFullName(),
                message.getContent(),
                message.getMessageType(),
                message.getIsRead(),
                message.getCreatedAt());
    }
}
