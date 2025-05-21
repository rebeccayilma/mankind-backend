-- Disable foreign key checks temporarily to avoid errors while truncating
SET FOREIGN_KEY_CHECKS = 0;

-- Clean dependent tables first
TRUNCATE TABLE inventory_logs;
TRUNCATE TABLE inventories;
TRUNCATE TABLE products;
TRUNCATE TABLE category;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;


-- Insert Categories with fixed IDs
INSERT INTO category (id, name, created_at, updated_at) VALUES
(1, 'GPUs', NOW(), NOW()),
(2, 'AI Hardware', NOW(), NOW()),
(3, 'SINGLE BOARD COMPUTERS', NOW(), NOW()),
(4, 'MICROCONTROLLERS', NOW(), NOW()),
(5, 'EDGE AI COMPUTING', NOW(), NOW()),
(6, 'IOT DEVELOPMENT', NOW(), NOW());


-- Insert Products with fixed IDs
INSERT INTO products (id, name, description, category_id, sku, brand, model, is_active, is_featured, created_at, updated_at) VALUES
(1, 'SpectraForce X Series', 'The SpectraForce X Series represents the pinnacle of gaming graphics technology. These cards deliver unparalleled performance for AAA gaming titles at 4K resolution with ray tracing enabled. Built on the latest architecture, they offer exceptional power efficiency while maintaining cool temperatures even under heavy loads.', 1, 'SFX-12G-001', 'SpectraForce', 'X-Series', TRUE, TRUE, NOW(), NOW()),
(2, 'NovaCore Vision', 'NovaCore Vision cards are specially designed for content creators and professionals who demand reliability and performance. These workstation-class GPUs excel at 3D modeling, video editing, and complex CAD operations with certified drivers for all major professional applications.', 1, 'NCV-24G-001', 'NovaCore', 'Vision-Pro', TRUE, FALSE, NOW(), NOW()),
(3, 'ThunderCore AI GPU', 'The ThunderCore AI GPU is designed specifically for high-throughput AI, machine learning, and data center workloads. With large memory bandwidth and optimized compute performance, it accelerates deep learning tasks and data processing tasks.', 1, 'TCG-32G-001', 'ThunderCore', 'AI-Series', TRUE, FALSE, NOW(), NOW()),
(4, 'FusionRender Series', 'FusionRender Series GPUs are engineered for the most demanding HPC and AI model training applications. These cards offer massive parallel processing power, ideal for simulations, large-scale model training, and scientific computing.', 1, 'FRS-48G-001', 'FusionRender', 'HPC-Series', TRUE, TRUE, NOW(), NOW()),
(5, 'QuantumMind Systems', 'QuantumMind Systems is a cutting-edge AI supercomputing platform, designed to accelerate deep learning and AI model training tasks. With multi-GPU configurations and optimized architecture, it empowers data scientists to handle complex workloads efficiently.', 2, 'QMS-8A-001', 'QuantumMind', 'SuperCompute', TRUE, TRUE, NOW(), NOW()),
(6, 'EdgeNexus Platform', 'EdgeNexus Platform is an AI-powered edge computing solution designed to optimize real-time data processing for IoT devices, robotics, and autonomous machines. It features low-latency computing and is ideal for edge AI deployments.', 2, 'ENP-XV-001', 'EdgeNexus', 'Edge-AI', TRUE, FALSE, NOW(), NOW()),
(7, 'RASPBERRY PI 4', 'The Raspberry Pi 4 is a versatile single-board computer ideal for DIY projects, education, and programming. With powerful features and connectivity, it can run multiple tasks simultaneously and is suitable for a range of applications.', 3, 'RPI4-4G-001', 'Raspberry Pi', 'Model 4B', TRUE, TRUE, NOW(), NOW()),
(8, 'ARDUINO UNO', 'The Arduino UNO is an open-source electronics platform used for building a wide range of interactive projects. With a microcontroller and simple programming environment, it''s a great tool for beginners and advanced makers alike.', 4, 'ARDU-UNO-001', 'Arduino', 'UNO R3', TRUE, TRUE, NOW(), NOW()),
(9, 'NVIDIA JETSON', 'NVIDIA Jetson is an AI computing platform built for accelerating GPU-intensive applications at the edge. With its compact size and powerful capabilities, it is ideal for IoT, robotics, and AI at the edge.', 5, 'NVJ-NANO-001', 'NVIDIA', 'Jetson Nano', TRUE, TRUE, NOW(), NOW()),
(10, 'ESP32 DEV KIT', 'The ESP32 Dev Kit is a powerful development board for building IoT applications with Wi-Fi and Bluetooth. It features dual-core processing and a range of peripherals for rapid prototyping.', 6, 'ESP32-DK-001', 'Espressif', 'ESP32', TRUE, TRUE, NOW(), NOW()),
(11, 'TEENSY 4.1', 'Teensy 4.1 is a USB-based development system with a powerful microcontroller that supports high-speed processing. It is ideal for audio processing, robotics, and advanced electronics projects.', 4, 'TNSY-41-001', 'PJRC', 'Teensy 4.1', TRUE, TRUE, NOW(), NOW()),
(12, 'NeuraFlow Suite', 'NeuraFlow Suite is a high-performance parallel computing platform and API built for accelerating AI workloads and large-scale data processing tasks. It provides efficient processing pipelines for AI systems.', 2, 'NFS-V100-001', 'NeuraFlow', 'V100-Series', TRUE, FALSE, NOW(), NOW()),
(13, 'DeepStream AI Studio', 'DeepStream AI Studio offers tools to build scalable AI-based video analytics applications for real-time processing in industries like retail, automotive, and security.', 2, 'DSAI-3080-001', 'DeepStream', 'RTX-Series', TRUE, FALSE, NOW(), NOW());

-- Insert Product Specifications
INSERT INTO product_specifications (product_id, spec_key, spec_value) VALUES
-- SpectraForce X Series (1)
(1, 'Memory', '12GB GDDR6X'),
(1, 'CUDA Cores', '5,888'),
(1, 'Base Clock', '1.5 GHz'),
(1, 'Boost Clock', '1.8 GHz'),
(1, 'TDP', '320W'),
(1, 'Recommended PSU', '750W'),
(1, 'Dimensions', '11.2 x 4.4 x 1.5 inches'),
(1, 'Weight', '1.2 kg'),
-- NovaCore Vision (2)
(2, 'Memory', '24GB GDDR6'),
(2, 'CUDA Cores', '4,608'),
(2, 'Base Clock', '1.3 GHz'),
(2, 'Boost Clock', '1.7 GHz'),
(2, 'TDP', '300W'),
(2, 'ECC Memory', 'Yes'),
(2, 'Dimensions', '12.5 x 5.5 x 1.7 inches'),
(2, 'Weight', '1.4 kg'),
-- ThunderCore AI GPU (3)
(3, 'Memory', '32GB GDDR6X'),
(3, 'CUDA Cores', '6,144'),
(3, 'Base Clock', '1.4 GHz'),
(3, 'Boost Clock', '1.9 GHz'),
(3, 'TDP', '350W'),
(3, 'Recommended PSU', '850W'),
(3, 'Dimensions', '13 x 5.7 x 2.0 inches'),
(3, 'Weight', '1.8 kg'),
-- FusionRender Series (4)
(4, 'Memory', '48GB GDDR6'),
(4, 'CUDA Cores', '8,192'),
(4, 'Base Clock', '1.2 GHz'),
(4, 'Boost Clock', '1.6 GHz'),
(4, 'TDP', '450W'),
(4, 'Recommended PSU', '1000W'),
(4, 'Dimensions', '14 x 6 x 2.5 inches'),
(4, 'Weight', '2.2 kg'),
-- QuantumMind Systems (5)
(5, 'CPU', 'Dual Intel Xeon Gold 6230'),
(5, 'Memory', '1TB DDR4 ECC'),
(5, 'Storage', '10TB SSD'),
(5, 'GPU', '8 x NVIDIA Tesla A100'),
(5, 'Dimensions', '24 x 20 x 8 inches'),
(5, 'Weight', '60 kg'),
-- EdgeNexus Platform (6)
(6, 'CPU', 'ARM Cortex-A76'),
(6, 'GPU', 'NVIDIA Jetson Xavier'),
(6, 'RAM', '32GB LPDDR4'),
(6, 'Storage', '256GB eMMC'),
(6, 'Dimensions', '8 x 5 x 3 inches'),
(6, 'Weight', '2.3 kg'),
-- Raspberry Pi 4 (7)
(7, 'CPU', 'Quad-core ARM Cortex-A72'),
(7, 'RAM', '4GB LPDDR4'),
(7, 'Storage', 'microSD card slot'),
(7, 'GPU', 'Broadcom VideoCore VI'),
(7, 'Dimensions', '3.4 x 2.2 x 1.1 inches'),
(7, 'Weight', '0.06 kg'),
-- Arduino UNO (8)
(8, 'Microcontroller', 'ATmega328P'),
(8, 'Flash Memory', '32KB'),
(8, 'Clock Speed', '16 MHz'),
(8, 'Digital I/O Pins', '14'),
(8, 'Analog I/O Pins', '6'),
(8, 'Dimensions', '2.7 x 2.1 x 0.8 inches'),
(8, 'Weight', '0.025 kg'),
-- NVIDIA Jetson (9)
(9, 'CPU', 'Quad-core ARM Cortex-A57'),
(9, 'GPU', 'NVIDIA Maxwell'),
(9, 'RAM', '4GB LPDDR4'),
(9, 'Storage', '16GB eMMC'),
(9, 'Dimensions', '3.5 x 3.2 x 1.1 inches'),
(9, 'Weight', '0.3 kg'),
-- ESP32 DEV KIT (10)
(10, 'CPU', 'Dual-core Tensilica LX6'),
(10, 'RAM', '520KB SRAM'),
(10, 'Storage', '4MB Flash'),
(10, 'Wi-Fi', '802.11 b/g/n'),
(10, 'Bluetooth', 'v4.2'),
(10, 'Dimensions', '3.0 x 1.5 x 0.5 inches'),
(10, 'Weight', '0.02 kg'),
-- Teensy 4.1 (11)
(11, 'Microcontroller', 'ARM Cortex-M7'),
(11, 'Flash Memory', '8MB'),
(11, 'Clock Speed', '600 MHz'),
(11, 'Digital I/O Pins', '55'),
(11, 'Dimensions', '2.4 x 1.4 x 0.3 inches'),
(11, 'Weight', '0.02 kg'),
-- NeuraFlow Suite (12)
(12, 'CPU', 'Dual Intel Xeon'),
(12, 'GPU', 'NVIDIA Tesla V100'),
(12, 'RAM', '128GB DDR4'),
(12, 'Storage', '5TB SSD'),
(12, 'Dimensions', '18 x 10 x 7 inches'),
(12, 'Weight', '15 kg'),
-- DeepStream AI Studio (13)
(13, 'CPU', 'Intel i7'),
(13, 'GPU', 'NVIDIA RTX 3080'),
(13, 'RAM', '16GB DDR4'),
(13, 'Storage', '1TB NVMe SSD'),
(13, 'Dimensions', '16 x 8 x 5 inches'),
(13, 'Weight', '10 kg');



-- Insert Product Images (make sure product_images has image_url column)

INSERT INTO product_images (product_id, image_url, images_order) VALUES
(1, 'https://m.media-amazon.com/images/I/71+PTOGv1mL.jpg', 1),
(2, 'https://i.extremetech.com/imagery/content-types/03jV5USras1qOUXTIGo7JIG/images-1.fill.size_670x377.v1691519408.png', 1),
(3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTFqL92lAigcdIEnDZ8knLGr8LOfEu00cbvdg&s', 1),
(4, 'https://www.networkworld.com/wp-content/uploads/2025/04/3966130-0-09907200-1745257104-original.jpg?quality=50&strip=all&w=1024', 1),
(5, 'https://alleninstitute.org/wp-content/uploads/2024/05/Quantumconsciousness2-ezgif.com-crop.jpg', 1),
(6, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRuOClJEpPkQssrdbTc-UKxEQPQPr2VWKxdMA&s', 1),
(7, 'https://m.media-amazon.com/images/I/616RFn6Jv5L._AC_UF894,1000_QL80_DpWeblab_.jpg', 1),
(8, 'https://store.arduino.cc/cdn/shop/files/A000066_03.front_934x700.jpg?v=1727098250', 1),
(9, 'https://d29g4g2dyqv443.cloudfront.net/sites/default/files/akamai/embedded%2Fimages%2FjetsonNano%2Fjetson_orin_nano-devkit-front_top-right-trimmed.jpg', 1),
(10, 'https://m.media-amazon.com/images/I/61o2ZUzB4XL._AC_UF894,1000_QL80_.jpg', 1),
(11, 'https://ca.robotshop.com/cdn/shop/files/teensy-41-usb-microcontroller-development-board-no-pins-2_1024x.webp?v=1720518161', 1),
(12, 'https://framerusercontent.com/images/b1WgDKAkCoPT9IqxSwrM7xfxMl8.png', 1),
(13, 'https://wpforms.com/wp-content/uploads/2024/08/google-ai-studio-logo.png', 1);

-- Insert Inventories
INSERT INTO inventories (product_id, available_quantity, reserved_quantity, sold_quantity, price, currency, active, created_at, updated_at) VALUES
(1, 10, 0, 0, 799.99, 'USD', TRUE, NOW(), NOW()),
(2, 10, 0, 0, 999.99, 'USD', TRUE, NOW(), NOW()),
(3, 10, 0, 0, 1499.99, 'USD', TRUE, NOW(), NOW()),
(4, 10, 0, 0, 2000.00, 'USD', TRUE, NOW(), NOW()),
(5, 5, 0, 0, 50000.00, 'USD', TRUE, NOW(), NOW()),
(6, 5, 0, 0, 10000.00, 'USD', TRUE, NOW(), NOW()),
(7, 50, 0, 0, 45.99, 'USD', TRUE, NOW(), NOW()),
(8, 100, 0, 0, 23.50, 'USD', TRUE, NOW(), NOW()),
(9, 25, 0, 0, 99.99, 'USD', TRUE, NOW(), NOW()),
(10, 75, 0, 0, 12.95, 'USD', TRUE, NOW(), NOW()),
(11, 100, 0, 0, 29.95, 'USD', TRUE, NOW(), NOW()),
(12, 5, 0, 0, 699.99, 'USD', TRUE, NOW(), NOW()),
(13, 5, 0, 0, 999.99, 'USD', TRUE, NOW(), NOW());


-- Insert Initial Inventory Logs
INSERT INTO inventory_logs (inventory_id, action_type, quantity, description, created_by, created_at) 
SELECT id, 'RESTOCK', available_quantity, 'Initial stock created', 'SYSTEM', NOW() FROM inventories;

