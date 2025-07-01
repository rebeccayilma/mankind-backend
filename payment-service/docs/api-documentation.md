# Payment Service API Documentation

## Overview

The Payment Service is responsible for handling credit/debit card payments using Stripe as the payment gateway. It provides endpoints for creating payment intents, checking payment status, and retrieving payment history.

## Authentication

All endpoints (except the webhook endpoint) require authentication:
- User endpoints require a valid JWT token with the `USER` role
- Admin endpoints require a valid JWT token with the `ADMIN` role

## Base URL

```
http://localhost:8084
```

## API Endpoints

### User Endpoints

#### Create Payment Intent

Creates a Stripe payment intent and returns the client_secret for frontend processing.

- **URL**: `/api/payments/create`
- **Method**: `POST`
- **Auth required**: Yes (USER role)
- **Request Body**:
```json
{
  "orderId": "order123",
  "amount": 100.00,
  "currency": "USD",
  "paymentMethod": "CREDIT_CARD",
  "description": "Payment for order #123"
}
```
- **Response**:
```json
{
  "paymentId": "550e8400-e29b-41d4-a716-446655440000",
  "paymentIntentId": "pi_3NxyzABCDEFGHIJKLMNOPQRST",
  "clientSecret": "pi_3NxyzABCDEFGHIJKLMNOPQRST_secret_abcdefghijklmnopqrstuvwxyz",
  "publicKey": "pk_test_51NxyzABCDEFGHIJKLMNOPQRSTUVWXYZ",
  "status": "PENDING"
}
```

#### Get Payment Status

Retrieves the status of a specific payment (only if it belongs to the logged-in user).

- **URL**: `/api/payments/status/{paymentId}`
- **Method**: `GET`
- **Auth required**: Yes (USER role)
- **URL Params**: `paymentId=[UUID]`
- **Response**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "orderId": "order123",
  "userId": "user123",
  "amount": 100.00,
  "currency": "USD",
  "paymentStatus": "SUCCESS",
  "paymentMethod": "CREDIT_CARD",
  "paymentGateway": "STRIPE",
  "transactionId": "txn_3NxyzABCDEFGHIJKLMNOPQRST",
  "createdAt": "2023-06-05T10:30:00",
  "updatedAt": "2023-06-05T10:35:00",
  "receiptUrl": "https://pay.stripe.com/receipts/..."
}
```

#### Get User Payments

Retrieves all payments made by the logged-in user.

- **URL**: `/api/payments/my-payments`
- **Method**: `GET`
- **Auth required**: Yes (USER role)
- **Response**: Array of payment objects

### Admin Endpoints

#### Get All Payments

Retrieves all payment records.

- **URL**: `/api/admin/payments`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **Response**: Array of payment objects

#### Get Payment by ID

Retrieves details of a payment by ID.

- **URL**: `/api/admin/payments/{paymentId}`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **URL Params**: `paymentId=[UUID]`
- **Response**: Payment object

#### Get Payments by User ID

Retrieves payment history for a specific user.

- **URL**: `/api/admin/payments/user/{userId}`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **URL Params**: `userId=[String]`
- **Response**: Array of payment objects

#### Get Payments by Status

Retrieves payments with a specific status.

- **URL**: `/api/admin/payments/status/{status}`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **URL Params**: `status=[PENDING|SUCCESS|FAILED]`
- **Response**: Array of payment objects

#### Get Payment Logs by Payment ID

Retrieves logs for a specific payment.

- **URL**: `/api/admin/payments/{paymentId}/logs`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **URL Params**: `paymentId=[UUID]`
- **Response**: Array of payment log objects

#### Get All Payment Logs

Retrieves all entries from PaymentLog table.

- **URL**: `/api/admin/payments/logs`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **Response**: Array of payment log objects

#### Get Payment Logs by Date Range

Retrieves logs created between two dates.

- **URL**: `/api/admin/payments/logs/date-range`
- **Method**: `GET`
- **Auth required**: Yes (ADMIN role)
- **Query Params**:
  - `startDate=[ISO DateTime]`
  - `endDate=[ISO DateTime]`
- **Response**: Array of payment log objects

### Webhook Endpoint

#### Handle Stripe Webhook

Processes Stripe payment status events.

- **URL**: `/api/payments/webhook`
- **Method**: `POST`
- **Auth required**: No
- **Headers**: `Stripe-Signature=[String]`
- **Request Body**: Stripe event payload
- **Response**: Success or error message

## Error Responses

All endpoints return standard HTTP status codes:
- `200 OK` - Request succeeded
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request parameters
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

Error responses include a message describing the error:
```json
{
  "timestamp": "2023-06-05T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid payment amount",
  "path": "/api/payments/create"
}
```

## Testing

You can test the API using the Swagger UI available at:
```
http://localhost:8084/swagger-ui.html
```