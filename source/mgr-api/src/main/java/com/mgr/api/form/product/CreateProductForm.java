package com.mgr.api.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductForm {
    @NotEmpty(message = "name is required")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @NotNull(message = "price is required")
    @ApiModelProperty(name = "price", required = true)
    private Double price;

    @NotNull(message = "stock is required")
    @ApiModelProperty(name = "stock", required = true)
    private Integer stock;

    @ApiModelProperty(name = "imagePath")
    private String imagePath;

    @ApiModelProperty(name = "brand")
    private String brand;

    @ApiModelProperty(name = "size")
    private String size;

    @ApiModelProperty(name = "color")
    private String color;

    @NotNull(message = "categoryId is required")
    @ApiModelProperty(name = "categoryId", required = true)
    private Long categoryId;
}