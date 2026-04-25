package com.mgr.api.dto.cart;

import com.mgr.api.dto.cartItem.CartItemDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CartDto {
    @ApiModelProperty(name = "totalPrice")
    private Double totalPrice;

    @ApiModelProperty(name = "items")
    private List<CartItemDto> items;
}