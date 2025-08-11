# Mankind Matrix Order Service

This service manages orders for the Mankind Matrix AI platform. It handles order creation, management, and tracking throughout the order lifecycle.

## Features

- **Order Management**: Create, read, update, and track orders
- **Order Number Generation**: Unique sequential numbers (date + time + random)
- **Status Management**: Order status + Payment status tracking
- **Coupon Integration**: Support for discount coupons with validation
- **Cart Integration**: Orders created from active carts, cart status changed to CONVERTED
- **Inventory Management**: Items marked as sold when order is created
- **Payment Tracking**: Link orders to payments

## Swagger Documentation

### Direct Access
- **Swagger UI**: http://localhost:8088/swagger-ui
- **OpenAPI Spec**: http://localhost:8088/v3/api-docs

### Through Gateway
- **Swagger UI**: http://localhost:8085/api/v1/orders/swagger-ui
- **API Endpoints**: http://localhost:8085/api/v1/orders/**

For detailed Swagger documentation, see [swagger-documentation.md](docs/swagger-documentation.md).

## Business Rules

### Order Creation Process

1. **User Authentication**: Get user based on JWT token
2. **Cart Retrieval**: 
   - Get current user's active cart automatically
   - Ensure cart status is ACTIVE
3. **Address Validation**:
   - Validate shipping address belongs to current user via `/users/me/addresses/{addressId}`
   - Validate billing address belongs to current user via `/users/me/addresses/{addressId}`
   - Both addresses must belong to the authenticated user
4. **Order Number Generation**: Generate unique order number (format: ORD-YYYYMMDD-HHMMSS-XXXXX)
5. **Coupon Processing**:
   - If coupon code provided, validate via `/coupons/validate` endpoint
   - Calculate discount based on coupon type (percentage or fixed amount)
   - Apply discount to final amount
6. **Order Creation**: Create order with PENDING status
7. **Order Items**: Convert cart items to order items
8. **Inventory Update**: Mark products as sold (change from reserved to sold)
9. **Cart Status Update**: Set cart status to CONVERTED
10. **Coupon Usage**: Mark coupon as used via `/coupons/use` endpoint with order ID
11. **Status History**: Record order status change

### Validation Rules

- **Cart Status**: Only ACTIVE carts can be converted to orders
- **Address Ownership**: Both shipping and billing addresses must belong to the current user
- **Coupon Validation**: Coupons must be validated before application
- **Inventory Check**: Products must have sufficient inventory before marking as sold

### Error Handling

- **CartValidationException**: No active cart, cart not active, or addresses don't belong to user
- **CouponValidationException**: Invalid or expired coupon codes
- **OrderCreationException**: Failures during order creation process
- **Global Exception Handler**: Consistent error response format

### Integration Points

- **Cart Service**: Get current user's cart, update cart status to CONVERTED
- **Coupon Service**: Validate coupon codes, mark coupons as used
- **Product Service**: Update inventory status, mark products as sold
- **User Service**: Get current user information, validate address ownership

### Request Format

```json
{
  "shippingAddressId": 1,
  "billingAddressId": 2,
  "couponCode": "SAVE20",
  "notes": "Additional notes for the order"
}
```

**Note**: No `cartId` required - the service automatically gets the current user's active cart.

## Order Status Lifecycle

- **PENDING**: Order created, waiting for confirmation
- **CONFIRMED**: Order confirmed, payment processed
- **PROCESSING**: Order being prepared for shipping
- **SHIPPED**: Order shipped to customer
- **DELIVERED**: Order delivered to customer
- **CANCELLED**: Order cancelled (can be cancelled before shipping)

## Payment Status

- **PENDING**: Payment not yet processed
- **PAID**: Payment completed successfully
- **FAILED**: Payment failed
- **REFUNDED**: Payment refunded
- **PARTIALLY_REFUNDED**: Partial refund issued


## Configuration

The service requires the following environment variables:
- `CART_SERVICE_URL`: URL for cart service (default: http://localhost:8082)
- `COUPON_SERVICE_URL`: URL for coupon service (default: http://localhost:8087)
- `PRODUCT_SERVICE_URL`: URL for product service (default: http://localhost:8080)
- `USER_SERVICE_URL`: URL for user service (default: http://localhost:8081)

