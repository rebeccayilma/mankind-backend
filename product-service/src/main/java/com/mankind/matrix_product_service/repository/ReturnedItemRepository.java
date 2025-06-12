package com.mankind.matrix_product_service.repository;

import com.mankind.matrix_product_service.model.ReturnedItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnedItemRepository extends JpaRepository<ReturnedItem, Long> {
    /**
     * Find all returned items by user ID
     * 
     * @param userId the ID of the user
     * @param pageable pagination information
     * @return a page of returned items
     */
    Page<ReturnedItem> findByUserId(Long userId, Pageable pageable);
    
    /**
     * Find all returned items by status
     * 
     * @param status the status of the returned items
     * @param pageable pagination information
     * @return a page of returned items
     */
    Page<ReturnedItem> findByStatus(String status, Pageable pageable);
    
    /**
     * Find all returned items by user ID and status
     * 
     * @param userId the ID of the user
     * @param status the status of the returned items
     * @param pageable pagination information
     * @return a page of returned items
     */
    Page<ReturnedItem> findByUserIdAndStatus(Long userId, String status, Pageable pageable);
}