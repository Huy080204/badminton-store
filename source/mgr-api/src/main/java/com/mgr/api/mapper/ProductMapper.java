package com.mgr.api.mapper;

import com.mgr.api.dto.product.ProductDto;
import com.mgr.api.form.product.CreateProductForm;
import com.mgr.api.form.product.UpdateProductForm;
import com.mgr.api.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.IterableMapping;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoryMapper.class, SellerMapper.class})
public interface ProductMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "categoryId", target = "category.id")
    Product fromCreateFormToEntity(CreateProductForm form);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "categoryId", target = "category.id")
    void mappingUpdateFormToEntity(UpdateProductForm form, @MappingTarget Product product);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "stock", target = "stock")
    @Mapping(source = "imagePath", target = "imagePath")
    @Mapping(source = "brand", target = "brand")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "color", target = "color")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "seller", target = "seller")
    ProductDto fromEntityToDto(Product product);

    @IterableMapping(elementTargetType = ProductDto.class)
    List<ProductDto> fromEntityListToDtoList(List<Product> list);
}