package com.mgr.api.dto.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "street")
    private String street;
    @ApiModelProperty(name = "zipCode")
    private String zipCode;
    @ApiModelProperty(name = "province")
    private Long provinceId;
    @ApiModelProperty(name = "district")
    private Long districtId;
    @ApiModelProperty(name = "commune")
    private Long communeId;
    @ApiModelProperty(name = "isDefault")
    private Boolean isDefault;
    @ApiModelProperty(name = "user_id")
    private Long userId;


}
