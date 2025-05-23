-- sample_cart_insert.sql
USE mankind_matrix_db;

-- Insert sample carts
INSERT INTO cart (user_id, session_id, status, created_at, updated_at)
VALUES
    (101, NULL, 'ACTIVE', NOW(), NOW()),
    (102, NULL, 'ACTIVE', NOW(), NOW()),
    (103, NULL, 'ACTIVE', NOW(), NOW()),
    (NULL, 'guest-session-123', 'ACTIVE', NOW(), NOW());

-- Insert sample cart items
INSERT INTO cart_item (cart_id, product_id, quantity, price_at_addition, created_at, updated_at)
VALUES
    (1, 201, 2, 19.99, NOW(), NOW()),
    (1, 202, 1, 29.99, NOW(), NOW()),
    (2, 201, 3, 19.99, NOW(), NOW()),
    (3, 203, 5, 9.99, NOW(), NOW()),
    (4, 204, 1, 14.99, NOW(), NOW());
