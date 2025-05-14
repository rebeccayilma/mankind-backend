package com.mankind.matrix_product_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private String name;

        @Column
        private String brand;

        @Column
        private String category;

        @Column(length = 1000)
        private String specifications;

        @Column
        private BigDecimal price;

        @Column
        private String imageUrl;

        @Column
        private boolean availability;

        @CreatedDate
        @Column(updatable = false, nullable = false)
        private LocalDateTime createTime;

        @Column
        @LastModifiedDate
        private LocalDateTime updateTime;

    }
