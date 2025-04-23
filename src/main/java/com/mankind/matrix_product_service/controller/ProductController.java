package com.mankind.matrix_product_service.controller;

import com.mankind.matrix_product_service.dto.ProductDTO;
import com.mankind.matrix_product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable  Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
