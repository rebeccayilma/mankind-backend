-- sample_data.sql
USE mankind_matrix_db;
INSERT INTO product (name, brand, category, specifications, price, image_url, availability, create_time, update_time)
VALUES
    ('SpectraForce X Series GPU', 'SpectraForce', 'Graphics Card', 'CUDA Cores: 10240, Memory: 24GB GDDR6X, PCIe 4.0 support', 1299.99, 'https://example.com/images/gpu.jpg', true, NOW(), NOW()),
    ('NeuraX AI Processor', 'NeuraTech', 'Processor', 'Cores: 64, Clock Speed: 3.2GHz, AI Boost Engine v2', 1799.00, 'https://example.com/images/neurax.jpg', true, NOW(), NOW());
