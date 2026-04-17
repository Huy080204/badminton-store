package com.mgr.api.form.seller;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UpdateSellerForm {
    @NotNull(message = "id is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @NotEmpty(message = "shopName is required")
    @ApiModelProperty(name = "shopName", required = true)
    private String shopName;

    @ApiModelProperty(name = "shopDescription")
    private String shopDescription;

    @ApiModelProperty(name = "status")
    private Integer status;
}
