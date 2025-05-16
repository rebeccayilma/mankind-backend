package com.mankind.matrix_product_service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private String specifications;
    private BigDecimal price;
    private String imageUrl;
    private boolean availability;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
