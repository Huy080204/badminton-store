package com.mgr.api.dto.cartItem;

import com.mgr.api.dto.product.ProductDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CartItemDto {
    private Long id;

    @ApiModelProperty(name = "product")
    private ProductDto product;

    @ApiModelProperty(name = "quantity")
    private Integer quantity;
}