INSERT INTO users (username, role, first_name, last_name, email, active, create_time, update_time)
VALUES
    ('admin', 'ADMIN', 'Admin', 'User', 'admin@examplle.com', true, NOW(), NOW()),
    ('jj', 'USER', 'John', 'Doe', 'john.doe@jj.com', true, NOW(), NOW());