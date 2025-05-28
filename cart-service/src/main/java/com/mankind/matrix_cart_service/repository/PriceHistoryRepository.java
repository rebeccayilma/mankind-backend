package com.mankind.matrix_cart_service.repository;

import com.mankind.matrix_cart_service.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    
    /**
     * Find all price history records for a specific cart item
     * @param cartItemId the ID of the cart item
     * @return list of price history records
     */
    List<PriceHistory> findByCartItemIdOrderByChangeDateDesc(Long cartItemId);
}