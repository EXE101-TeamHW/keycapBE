CREATE INDEX IF NOT EXISTS idx_tickets_created_at
    ON public.tickets (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_tickets_status
    ON public.tickets (status);
CREATE INDEX IF NOT EXISTS idx_tickets_request_id
    ON public.tickets (request_id);
CREATE INDEX IF NOT EXISTS idx_tickets_assigned_staff_id
    ON public.tickets (assigned_staff_id);

CREATE INDEX IF NOT EXISTS idx_orders_created_at
    ON public.orders (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_orders_status
    ON public.orders (status);
CREATE INDEX IF NOT EXISTS idx_orders_user_id
    ON public.orders (user_id);
CREATE INDEX IF NOT EXISTS idx_orders_ticket_id
    ON public.orders (ticket_id);
CREATE INDEX IF NOT EXISTS idx_orders_assigned_staff_id
    ON public.orders (assigned_staff_id);

CREATE INDEX IF NOT EXISTS idx_order_items_order_id
    ON public.order_items (order_id);
CREATE INDEX IF NOT EXISTS idx_conversations_order_id
    ON public.conversations (order_id);
CREATE INDEX IF NOT EXISTS idx_conversations_ticket_id
    ON public.conversations (ticket_id);
CREATE INDEX IF NOT EXISTS idx_conversations_customer_id_created_at
    ON public.conversations (customer_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_messages_conversation_unread
    ON public.messages (conversation_id, is_read, sender_id);
CREATE INDEX IF NOT EXISTS idx_custom_requests_user_id
    ON public.custom_requests (user_id);
