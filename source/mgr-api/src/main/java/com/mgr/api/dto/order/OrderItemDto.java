package com.mgr.api.dto.order;

import com.mgr.api.dto.product.ProductDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemDto {
    private Long id;

    @ApiModelProperty(name = "product")
    private ProductDto product;

    @ApiModelProperty(name = "quantity")
    private Integer quantity;

    @ApiModelProperty(name = "price")
    private Double price;
}