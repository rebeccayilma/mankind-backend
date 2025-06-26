-- payment_tables.sql
USE mankind_matrix_db;

-- Create payment table
CREATE TABLE IF NOT EXISTS payment (
  id BINARY(16) PRIMARY KEY,
  order_id VARCHAR(100) NOT NULL,
  user_id VARCHAR(100) NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  payment_status ENUM('PENDING', 'SUCCESS', 'FAILED') DEFAULT 'PENDING',
  payment_method ENUM('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'APPLE_PAY', 'GOOGLE_PAY') NOT NULL,
  payment_gateway ENUM('STRIPE', 'PAYPAL', 'BRAINTREE') DEFAULT 'STRIPE',
  transaction_id VARCHAR(100),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create payment_log table
CREATE TABLE IF NOT EXISTS payment_log (
  id BINARY(16) PRIMARY KEY,
  payment_id BINARY(16) NOT NULL,
  log_type ENUM('INFO', 'WARNING', 'ERROR', 'DEBUG', 'TRANSACTION') NOT NULL,
  log_message TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (payment_id) REFERENCES payment(id)
);

-- Add indexes for better performance
CREATE INDEX idx_payment_user_id ON payment(user_id);
CREATE INDEX idx_payment_order_id ON payment(order_id);
CREATE INDEX idx_payment_status ON payment(payment_status);
CREATE INDEX idx_payment_transaction_id ON payment(transaction_id);
CREATE INDEX idx_payment_log_payment_id ON payment_log(payment_id);
CREATE INDEX idx_payment_log_log_type ON payment_log(log_type);
CREATE INDEX idx_payment_log_created_at ON payment_log(created_at);