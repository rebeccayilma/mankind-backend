package com.mankind.matrix_product_service.service;

import com.mankind.matrix_product_service.dto.ProductDTO;
import com.mankind.matrix_product_service.exception.ProductNotFoundException;
import com.mankind.matrix_product_service.mapper.ProductMapper;
import com.mankind.matrix_product_service.model.Product;
import com.mankind.matrix_product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDTO getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.map(productMapper::toDto).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toDto);
    }

    public ProductDTO createProduct(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        product.setCreateTime(LocalDateTime.now());
        product.setUpdateTime(LocalDateTime.now());
        return productMapper.toDto(productRepository.save(product));
    }
}
