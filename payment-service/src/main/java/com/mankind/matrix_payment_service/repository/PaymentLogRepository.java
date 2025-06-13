package com.mankind.matrix_payment_service.repository;

import com.mankind.matrix_payment_service.model.LogType;
import com.mankind.matrix_payment_service.model.Payment;
import com.mankind.matrix_payment_service.model.PaymentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentLogRepository extends JpaRepository<PaymentLog, UUID> {
    
    /**
     * Find all logs for a specific payment
     * @param payment the payment entity
     * @return list of payment logs
     */
    List<PaymentLog> findByPayment(Payment payment);
    
    /**
     * Find all logs for a specific payment ID
     * @param paymentId the payment ID
     * @return list of payment logs
     */
    List<PaymentLog> findByPaymentId(UUID paymentId);
    
    /**
     * Find logs by log type
     * @param logType the log type
     * @return list of payment logs
     */
    List<PaymentLog> findByLogType(LogType logType);
    
    /**
     * Find logs created after a specific date
     * @param date the date
     * @return list of payment logs
     */
    List<PaymentLog> findByCreatedAtAfter(LocalDateTime date);
    
    /**
     * Find logs created between two dates
     * @param startDate the start date
     * @param endDate the end date
     * @return list of payment logs
     */
    List<PaymentLog> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find logs by payment ID and log type
     * @param paymentId the payment ID
     * @param logType the log type
     * @return list of payment logs
     */
    List<PaymentLog> findByPaymentIdAndLogType(UUID paymentId, LogType logType);
}