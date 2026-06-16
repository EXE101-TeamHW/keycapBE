ALTER TABLE public.orders
    ADD COLUMN IF NOT EXISTS payos_order_code BIGINT;

CREATE UNIQUE INDEX IF NOT EXISTS uq_orders_payos_order_code
    ON public.orders (payos_order_code)
    WHERE payos_order_code IS NOT NULL;
