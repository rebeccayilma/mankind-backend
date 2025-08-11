package com.mankind.matrix_order_service.repository;

import com.mankind.matrix_order_service.model.OrderPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPaymentRepository extends JpaRepository<OrderPayment, Long> {
    
    List<OrderPayment> findByOrderId(Long orderId);
    
    Optional<OrderPayment> findByPaymentId(String paymentId);
}
