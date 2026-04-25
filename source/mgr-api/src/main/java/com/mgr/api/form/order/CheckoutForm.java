package com.mgr.api.form.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class CheckoutForm {
    @NotNull(message = "addressId is required")
    @ApiModelProperty(name = "addressId", required = true)
    private Long addressId;

    @NotNull(message = "paymentMethod is required")
    @ApiModelProperty(name = "paymentMethod", required = true)
    private Integer paymentMethod; // 1: COD, 2: Online
}