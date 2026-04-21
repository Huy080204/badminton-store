package com.mgr.api.form.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductForm {
    @NotNull(message = "id is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

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

    @ApiModelProperty(name = "categoryId")
    private Long categoryId;

    @ApiModelProperty(name = "status")
    private Integer status;
}