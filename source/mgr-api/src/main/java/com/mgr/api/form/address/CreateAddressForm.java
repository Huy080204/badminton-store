package com.mgr.api.form.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateAddressForm {
    @ApiModelProperty(name = "street", required = true)
    @NotEmpty(message = "street cannot be empty")
    private String street;

    @ApiModelProperty(name = "zipCode")
    private String zipCode;

    @ApiModelProperty(name = "isDefault")
    @NotNull(message = "isDefault cannot be null")
    private Boolean isDefault;

    @ApiModelProperty(name = "userId", required = true)
    @NotNull(message = "userId cannot be null")
    private Long userId;

    @ApiModelProperty(name = "provinceId")
    private Long provinceId;

    @ApiModelProperty(name = "districtId")
    private Long districtId;

    @ApiModelProperty(name = "communeId")
    private Long communeId;
}
