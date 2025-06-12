package com.mankind.matrix_payment_service.service;

import com.mankind.matrix_payment_service.dto.PaymentIntentResponseDto;
import com.mankind.matrix_payment_service.dto.PaymentLogResponseDto;
import com.mankind.matrix_payment_service.dto.PaymentRequestDto;
import com.mankind.matrix_payment_service.dto.PaymentResponseDto;
import com.mankind.matrix_payment_service.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaymentService {

    /**
     * Create a payment intent with Stripe
     * @param paymentRequestDto the payment request
     * @param userId the authenticated user ID
     * @return the payment intent response
     */
    PaymentIntentResponseDto createPaymentIntent(PaymentRequestDto paymentRequestDto, String userId);
    
    /**
     * Get payment by ID
     * @param id the payment ID
     * @return the payment
     */
    PaymentResponseDto getPaymentById(UUID id);
    
    /**
     * Get payment by ID and user ID (for user-specific access control)
     * @param id the payment ID
     * @param userId the user ID
     * @return the payment if it belongs to the user
     */
    PaymentResponseDto getPaymentByIdAndUserId(UUID id, String userId);
    
    /**
     * Get all payments for a specific user
     * @param userId the user ID
     * @return list of payments
     */
    List<PaymentResponseDto> getPaymentsByUserId(String userId);
    
    /**
     * Get all payments (admin only)
     * @return list of all payments
     */
    List<PaymentResponseDto> getAllPayments();
    
    /**
     * Get payments by status (admin only)
     * @param status the payment status
     * @return list of payments with the specified status
     */
    List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status);
    
    /**
     * Get payment logs for a specific payment (admin only)
     * @param paymentId the payment ID
     * @return list of payment logs
     */
    List<PaymentLogResponseDto> getPaymentLogsByPaymentId(UUID paymentId);
    
    /**
     * Get all payment logs (admin only)
     * @return list of all payment logs
     */
    List<PaymentLogResponseDto> getAllPaymentLogs();
    
    /**
     * Get payment logs created between two dates (admin only)
     * @param startDate the start date
     * @param endDate the end date
     * @return list of payment logs
     */
    List<PaymentLogResponseDto> getPaymentLogsBetweenDates(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Handle Stripe webhook event
     * @param payload the webhook payload
     * @param signature the webhook signature
     * @return true if the webhook was processed successfully
     */
    boolean handleStripeWebhook(String payload, String signature);
}