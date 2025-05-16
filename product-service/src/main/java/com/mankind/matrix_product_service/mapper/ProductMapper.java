package com.mankind.matrix_product_service.mapper;

import com.mankind.matrix_product_service.dto.ProductDTO;
import com.mankind.matrix_product_service.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductDTO dto);
}
