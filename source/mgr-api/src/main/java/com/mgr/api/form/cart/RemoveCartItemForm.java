package com.mgr.api.form.cart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RemoveCartItemForm {
    @NotNull(message = "cartItemId is required")
    @ApiModelProperty(name = "cartItemId", required = true)
    private Long cartItemId;
}
