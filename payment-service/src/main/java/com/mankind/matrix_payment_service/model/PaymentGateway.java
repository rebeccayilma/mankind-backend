package com.mankind.matrix_payment_service.model;

/**
 * Enum representing the payment gateway used for processing
 */
public enum PaymentGateway {
    STRIPE,
    PAYPAL,
    SQUARE,
    BRAINTREE,
    ADYEN
}