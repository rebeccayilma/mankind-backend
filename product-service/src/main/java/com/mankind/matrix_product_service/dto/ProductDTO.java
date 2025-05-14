package com.mankind.matrix_product_service.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private String name;
    private String brand;
    private String category;
    private String specifications;
    private Double price;
    private String imageUrl;
    private boolean availability;
}
