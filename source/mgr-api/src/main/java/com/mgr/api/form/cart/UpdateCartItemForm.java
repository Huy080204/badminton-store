package com.mgr.api.form.cart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCartItemForm {
    @NotNull(message = "cartItemId is required")
    @ApiModelProperty(name = "cartItemId", required = true)
    private Long cartItemId;

    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    @ApiModelProperty(name = "quantity", required = true)
    private Integer quantity;
}