UPDATE tickets AS ticket
SET assigned_staff_id = orders.assigned_staff_id,
    status = 'IN_REVIEW',
    updated_at = CURRENT_TIMESTAMP
FROM orders
WHERE orders.ticket_id = ticket.id
  AND orders.status = 'CONFIRMED'
  AND orders.assigned_staff_id IS NOT NULL
  AND ticket.status = 'PENDING';

UPDATE custom_requests AS request
SET status = 'IN_REVIEW',
    updated_at = CURRENT_TIMESTAMP
FROM tickets AS ticket
JOIN orders ON orders.ticket_id = ticket.id
WHERE ticket.request_id = request.id
  AND orders.status = 'CONFIRMED'
  AND orders.assigned_staff_id IS NOT NULL
  AND request.status = 'PENDING';
