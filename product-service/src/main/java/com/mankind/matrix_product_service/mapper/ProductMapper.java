package com.mankind.matrix_product_service.mapper;

import com.mankind.matrix_product_service.dto.ProductDTO;
import com.mankind.matrix_product_service.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "brand", source = "brand"),
        @Mapping(target = "category", source = "category"),
        @Mapping(target = "specifications", source = "specifications"),
        @Mapping(target = "price", source = "price"),
        @Mapping(target = "imageUrl", source = "imageUrl"),
        @Mapping(target = "availability", source = "availability"),
        @Mapping(target = "createTime", source = "createTime"),
        @Mapping(target = "updateTime", source = "updateTime")
    })
    ProductDTO toDto(Product product);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "name"),
        @Mapping(target = "brand", source = "brand"),
        @Mapping(target = "category", source = "category"),
        @Mapping(target = "specifications", source = "specifications"),
        @Mapping(target = "price", source = "price"),
        @Mapping(target = "imageUrl", source = "imageUrl"),
        @Mapping(target = "availability", source = "availability"),
        @Mapping(target = "createTime", source = "createTime"),
        @Mapping(target = "updateTime", source = "updateTime")
    })
    Product toEntity(ProductDTO dto);
}
