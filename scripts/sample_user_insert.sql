INSERT INTO users (username, role, first_name, last_name, email, password, active, create_time, update_time)
VALUES
    ('admin', 'ADMIN', 'Admin', 'User', 'admin@examplle.com',
     '$2a$10$7Hh92IHHhVWdfv3JdZoHzOcqnwGcQ5sdn/BjsOyzK/Jg1KHa1vpgK', true, NOW(), NOW()),
# pw = admin123
    ('jj', 'USER', 'John', 'Doe', 'john.doe@jj.com',
     '$2a$10$Xq1ljrF/jEyRmrkzPlQbke5q3Xo5EeQbh9s6BvJWnqJJxei8sAiOq', true, NOW(), NOW());
# pw = password123