-- cart_tables.sql
USE mankind_matrix_db;

-- Create cart table
CREATE TABLE IF NOT EXISTS cart (
  id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT,
  session_id VARCHAR(100), -- For guest users
  status ENUM('ACTIVE', 'CONVERTED', 'ABANDONED', 'REMOVED') DEFAULT 'ACTIVE',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Create cart item table
CREATE TABLE IF NOT EXISTS cart_item (
  id INT PRIMARY KEY AUTO_INCREMENT,
  cart_id INT NOT NULL,
  product_id INT NOT NULL,
  quantity INT NOT NULL DEFAULT 1,
  price_at_addition DECIMAL(10, 2) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (cart_id) REFERENCES cart(id)
);

-- Create price history table
CREATE TABLE IF NOT EXISTS price_history (
  id INT PRIMARY KEY AUTO_INCREMENT,
  cart_item_id INT NOT NULL,
  old_price DECIMAL(10, 2) NOT NULL,
  new_price DECIMAL(10, 2) NOT NULL,
  change_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (cart_item_id) REFERENCES cart_item(id)
);

-- Add indexes for better performance
CREATE INDEX idx_cart_user_id ON cart(user_id);
CREATE INDEX idx_cart_session_id ON cart(session_id);
CREATE INDEX idx_cart_status ON cart(status);
CREATE INDEX idx_cart_item_cart_id ON cart_item(cart_id);
CREATE INDEX idx_cart_item_product_id ON cart_item(product_id);
CREATE INDEX idx_price_history_cart_item_id ON price_history(cart_item_id);