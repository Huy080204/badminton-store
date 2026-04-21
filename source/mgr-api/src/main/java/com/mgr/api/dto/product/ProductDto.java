package com.mgr.api.dto.product;

import com.mgr.api.dto.ABasicAdminDto;
import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.dto.seller.SellerDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "price")
    private Double price;

    @ApiModelProperty(name = "stock")
    private Integer stock;

    @ApiModelProperty(name = "imagePath")
    private String imagePath;

    @ApiModelProperty(name = "brand")
    private String brand;

    @ApiModelProperty(name = "size")
    private String size;

    @ApiModelProperty(name = "color")
    private String color;

    @ApiModelProperty(name = "category")
    private CategoryDto category;

    @ApiModelProperty(name = "seller")
    private SellerDto seller;
}