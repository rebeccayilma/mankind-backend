package com.mankind.matrix_payment_service.repository;

import com.mankind.matrix_payment_service.model.Payment;
import com.mankind.matrix_payment_service.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    
    /**
     * Find all payments for a specific user
     * @param userId the ID of the user
     * @return list of payments
     */
    List<Payment> findByUserId(String userId);
    
    /**
     * Find payment by ID and user ID (for user-specific access control)
     * @param id the payment ID
     * @param userId the user ID
     * @return the payment if found
     */
    Optional<Payment> findByIdAndUserId(UUID id, String userId);
    
    /**
     * Find payments by status
     * @param paymentStatus the payment status
     * @return list of payments
     */
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);
    
    /**
     * Find payments by user ID and status
     * @param userId the user ID
     * @param paymentStatus the payment status
     * @return list of payments
     */
    List<Payment> findByUserIdAndPaymentStatus(String userId, PaymentStatus paymentStatus);
    
    /**
     * Find payment by transaction ID
     * @param transactionId the transaction ID
     * @return the payment if found
     */
    Optional<Payment> findByTransactionId(String transactionId);
    
    /**
     * Find payment by order ID
     * @param orderId the order ID
     * @return the payment if found
     */
    Optional<Payment> findByOrderId(String orderId);
}