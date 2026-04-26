
INSERT INTO users (id, name, email)

INSERT INTO item_requests (id, description, requester_id, created)

INSERT INTO items (id, name, description, available, owner_id, request_id)

INSERT INTO bookings (id, start_date, end_date, item_id, booker_id, status)

INSERT INTO comments (id, text, item_id, author_id, created)