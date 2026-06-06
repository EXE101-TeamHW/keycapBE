package com.keycap.keycapdesign.service;

import com.keycap.keycapdesign.dto.chat.CloseConversationRequest;
import com.keycap.keycapdesign.dto.chat.ConversationCreateRequest;
import com.keycap.keycapdesign.dto.chat.ConversationResponse;
import com.keycap.keycapdesign.dto.chat.MarkReadRequest;
import com.keycap.keycapdesign.dto.chat.MessageRequest;
import com.keycap.keycapdesign.dto.chat.MessageResponse;
import com.keycap.keycapdesign.entity.Conversation;
import com.keycap.keycapdesign.entity.Message;
import com.keycap.keycapdesign.entity.Order;
import com.keycap.keycapdesign.entity.User;
import com.keycap.keycapdesign.enums.ConversationStatus;
import com.keycap.keycapdesign.enums.MessageType;
import com.keycap.keycapdesign.enums.Role;
import com.keycap.keycapdesign.exception.BadRequestException;
import com.keycap.keycapdesign.exception.ResourceNotFoundException;
import com.keycap.keycapdesign.repository.ConversationRepository;
import com.keycap.keycapdesign.repository.MessageRepository;
import com.keycap.keycapdesign.repository.OrderRepository;
import com.keycap.keycapdesign.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ConversationService(ConversationRepository conversationRepository, MessageRepository messageRepository,
                               UserRepository userRepository, OrderRepository orderRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public ConversationResponse createConversation(ConversationCreateRequest request, Long viewerId) {
        if (request.getCustomerId() == null) {
            throw new BadRequestException("customerId is required");
        }
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

        if (request.getTicketId() != null) {
            Long finalTicketId = request.getTicketId();
            User finalStaff = staff;
            return conversationRepository.findByTicketId(finalTicketId)
                    .map(existing -> {
                        if (existing.getStaff() == null && finalStaff != null) {
                            existing.setStaff(finalStaff);
                            conversationRepository.save(existing);
                        }
                        return toConversationResponse(existing, viewerId);
                    })
                    .orElseGet(() -> createConversation(customer, finalStaff, request.getOrderId(), finalTicketId, viewerId));
        }

        Order order = null;
        if (request.getOrderId() != null) {
            order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
            validateOrderParticipants(order, customer, staff);
            User assignedStaff = getAssignedStaff(order);
            if (staff == null) {
                staff = assignedStaff;
            }

            User finalStaff = staff;
            Long finalOrderId = order.getId();
            
            // Check by orderId first
            java.util.Optional<Conversation> existingOpt = conversationRepository.findByOrderId(finalOrderId);
            if (existingOpt.isPresent()) {
                Conversation existing = existingOpt.get();
                if (existing.getStaff() == null && finalStaff != null) {
                    existing.setStaff(finalStaff);
                    conversationRepository.save(existing);
                }
                return toConversationResponse(existing, viewerId);
            }
            
            // If order has ticket, check by ticketId
            if (order.getTicket() != null) {
                java.util.Optional<Conversation> byTicket = conversationRepository.findByTicketId(order.getTicket().getId());
                if (byTicket.isPresent()) {
                    Conversation existing = byTicket.get();
                    existing.setOrderId(finalOrderId); // update order link
                    if (existing.getStaff() == null && finalStaff != null) {
                        existing.setStaff(finalStaff);
                    }
                    conversationRepository.save(existing);
                    return toConversationResponse(existing, viewerId);
                }
            }

            return createConversation(customer, finalStaff, finalOrderId, order.getTicket() != null ? order.getTicket().getId() : null, viewerId);
        }

        return createConversation(customer, staff, null, null, viewerId);
    }

    private ConversationResponse createConversation(User customer, User staff, Long orderId, Long ticketId, Long viewerId) {
        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setStaff(staff);
        conversation.setOrderId(orderId);
        conversation.setTicketId(ticketId);
        conversation.setStatus(ConversationStatus.OPEN);
        conversationRepository.save(conversation);
        return toConversationResponse(conversation, viewerId);
    }

    @Transactional
    public ConversationResponse getOrCreateConversationForOrder(Long customerId, Long staffId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        java.util.Optional<Conversation> byOrder = conversationRepository.findByOrderId(orderId);
        if (byOrder.isPresent()) {
            Conversation existing = byOrder.get();
            if (staffId != null && (existing.getStaff() == null || !existing.getStaff().getId().equals(staffId))) {
                User newStaff = userRepository.findById(staffId)
                        .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
                existing.setStaff(newStaff);
                conversationRepository.save(existing);
            }
            return toConversationResponse(existing, customerId);
        }

        if (order.getTicket() != null) {
            java.util.Optional<Conversation> byTicket = conversationRepository.findByTicketId(order.getTicket().getId());
            if (byTicket.isPresent()) {
                Conversation existing = byTicket.get();
                existing.setOrderId(orderId);
                if (staffId != null && (existing.getStaff() == null || !existing.getStaff().getId().equals(staffId))) {
                    User newStaff = userRepository.findById(staffId)
                            .orElseThrow(() -> new ResourceNotFoundException("Staff not found"));
                    existing.setStaff(newStaff);
                }
                conversationRepository.save(existing);
                return toConversationResponse(existing, customerId);
            }
        }

        User customer = getUser(customerId);
        User staff = staffId != null ? userRepository.findById(staffId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff not found")) : null;
        Conversation conversation = new Conversation();
        conversation.setCustomer(customer);
        conversation.setStaff(staff);
        conversation.setOrderId(orderId);
        if (order.getTicket() != null) {
            conversation.setTicketId(order.getTicket().getId());
        }
        conversation.setStatus(ConversationStatus.OPEN);
        conversationRepository.save(conversation);
        return toConversationResponse(conversation, customerId);
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

    private void validateOrderParticipants(Order order, User customer, User staff) {
        if (order.getUser() == null || !order.getUser().getId().equals(customer.getId())) {
            throw new BadRequestException("Order does not belong to customer");
        }
        User assignedStaff = getAssignedStaff(order);
        if (staff != null && assignedStaff != null && !assignedStaff.getId().equals(staff.getId())) {
            throw new BadRequestException("Staff is not assigned to this order");
        }
        if (staff != null && assignedStaff == null) {
            throw new BadRequestException("Order has no assigned staff");
        }
    }

    private User getAssignedStaff(Order order) {
        if (order.getAssignedStaff() != null) {
            return order.getAssignedStaff();
        }
        if (order.getTicket() != null) {
            return order.getTicket().getAssignedStaff();
        }
        return null;
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
                conversation.getOrderId(),
                conversation.getStatus(),
                unreadCount,
                conversation.getCreatedAt(),
                conversation.getTicketId());
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
