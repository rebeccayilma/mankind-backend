# Order Service

This service handles order creation, management, and processing for the Mankind e-commerce platform.

## Features

- Create orders from user carts
- Update existing orders with new information
- Handle coupon discounts
- Manage order status and payment status
- Support for different delivery types (Standard/Express)
- Shipping date scheduling
- Order history and admin management

## New Fields Added

The order creation endpoint now supports the following additional fields:

### CreateOrderRequest
- `couponCode`: Optional coupon code for discounts
- `deliveryType`: Delivery type - "STANDARD" or "EXPRESS"
- `notes`: Additional notes for the order
- `shippingAddressId`: ID of the shipping address
- `shippingDate`: Date for delivery
- `shippingValue`: Cost of shipping

### Order Model
- `deliveryType`: Enum field for STANDARD/EXPRESS delivery
- `shippingDate`: Date field for scheduled delivery

## API Endpoints

### POST /orders
Creates a new order from the current user's active cart or updates an existing order.

**Request Body:**
```json
{
  "shippingAddressId": 1,
  "shippingValue": 15.99,
  "couponCode": "SAVE20",
  "notes": "Please deliver to the front door",
  "deliveryType": "EXPRESS",
  "shippingDate": "2024-12-25"
}
```

**Response:**
```json
{
  "id": 1,
  "orderNumber": "ORD-20241201-143022-12345",
  "userId": 123,
  "cartId": 1,
  "status": "PENDING",
  "paymentStatus": "PENDING",
  "subtotal": 100.00,
  "tax": 8.50,
  "discounts": 20.00,
  "total": 88.50,
  "shippingValue": 15.99,
  "shippingAddressId": 1,
  "deliveryType": "EXPRESS",
  "shippingDate": "2024-12-25",
  "notes": "Please deliver to the front door",
  "couponCode": "SAVE20",
  "discountType": "PERCENTAGE",
  "items": [...],
  "createdAt": "2024-12-01T14:30:22",
  "updatedAt": "2024-12-01T14:30:22"
}
```

## Order Update Behavior

When an order already exists for a cart:
1. **Always updates** the order with new request data
2. **Recalculates totals** based on current cart items and coupon
3. **Updates order items** to match current cart
4. **Preserves order ID** and order number
5. **Creates status history** for tracking changes

## Database Setup

Run the SQL script in `scripts/order_tables.sql` to create the required tables:

```bash
mysql -u matrix_user -p mankind_matrix_db < scripts/order_tables.sql
```

## Configuration

The service uses the following environment variables:
- `DB_HOST`: Database host (default: localhost)
- `DB_PORT`: Database port (default: 3306)
- `DB_NAME`: Database name (default: mankind_matrix_db)
- `DB_USERNAME`: Database username (default: matrix_user)
- `DB_PASSWORD`: Database password (default: matrix_pass)

## Dependencies

- Spring Boot 3.x
- Spring Data JPA
- MySQL Database
- Feign Clients for Cart, Coupon, and User services
- JWT Authentication via Keycloak

