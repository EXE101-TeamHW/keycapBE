DO $$
BEGIN
    IF to_regclass('public.custom_requests') IS NOT NULL
       AND NOT EXISTS (
           SELECT 1
           FROM pg_constraint
           WHERE conrelid = 'public.custom_requests'::regclass
             AND contype = 'p'
       ) THEN
        IF EXISTS (SELECT 1 FROM public.custom_requests WHERE id IS NULL) THEN
            RAISE EXCEPTION 'Cannot add primary key to custom_requests: id contains NULL values';
        END IF;

        IF EXISTS (
            SELECT id
            FROM public.custom_requests
            GROUP BY id
            HAVING COUNT(*) > 1
        ) THEN
            RAISE EXCEPTION 'Cannot add primary key to custom_requests: id contains duplicate values';
        END IF;

        ALTER TABLE public.custom_requests
            ADD CONSTRAINT custom_requests_pkey PRIMARY KEY (id);
    END IF;
END
$$;
