package com.mgr.api.form.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAddressForm {
    @ApiModelProperty(name = "id", required = true)
    @NotNull(message = "id cannot be null")
    private Long id;

    @ApiModelProperty(name = "street")
    private String street;

    @ApiModelProperty(name = "zipCode")
    private String zipCode;

    @ApiModelProperty(name = "isDefault")
    private Boolean isDefault;

    @ApiModelProperty(name = "provinceId", required = true)
    private Long provinceId;

    @ApiModelProperty(name = "districtId", required = true)
    private Long districtId;

    @ApiModelProperty(name = "communeId", required = true)
    private Long communeId;
}
