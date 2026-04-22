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

    @ApiModelProperty(name = "provinceId")
    private Long provinceId;

    @ApiModelProperty(name = "districtId")
    private Long districtId;

    @ApiModelProperty(name = "communeId")
    private Long communeId;
}
