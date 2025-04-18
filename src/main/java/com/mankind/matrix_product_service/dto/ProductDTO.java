package com.mankind.matrix_product_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private String specifications;
    private Double price;
    private String imageUrl;
    private boolean availability;
}
