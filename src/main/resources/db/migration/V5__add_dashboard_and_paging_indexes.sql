CREATE INDEX IF NOT EXISTS idx_orders_updated_at
    ON public.orders (updated_at DESC);

CREATE INDEX IF NOT EXISTS idx_orders_created_status
    ON public.orders (created_at, status);

CREATE INDEX IF NOT EXISTS idx_orders_type_payment_status
    ON public.orders (type, payment_status, status);

CREATE INDEX IF NOT EXISTS idx_custom_requests_user_created_at
    ON public.custom_requests (user_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_conversations_staff_created_at
    ON public.conversations (staff_id, created_at DESC);
