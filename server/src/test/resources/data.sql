INSERT INTO users (name, email) VALUES
('Max', 'max@mail.com'),
('Anna', 'anna@mail.com'),
('John', 'john@mail.com');

INSERT INTO item_requests (description, requester_id, created)
VALUES ('Need drill', 1, CURRENT_TIMESTAMP);

INSERT INTO items (name, description, available, owner_id, request_id)
 VALUES
 ('Drill', 'Power drill', true, 2, 1),
 ('Hammer', 'Steel hammer', true, 2, NULL);

INSERT INTO bookings (id, start_date, end_date, status, item_id, booker_id) VALUES
(1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'APPROVED', 1, 1);

INSERT INTO comments (text, item_id, author_id, created)
VALUES ('Good item', 1, 1, CURRENT_TIMESTAMP);