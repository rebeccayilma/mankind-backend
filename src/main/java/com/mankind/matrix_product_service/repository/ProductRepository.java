package com.mankind.matrix_product_service.repository;

import com.mankind.matrix_product_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public Page<Product> findByCategoryIgnoreCase(String category, Pageable pageable);
    
}

