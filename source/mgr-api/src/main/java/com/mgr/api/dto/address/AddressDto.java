package com.mgr.api.dto.address;

import com.mgr.api.dto.ABasicAdminDto;
import com.mgr.api.dto.nation.NationDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddressDto extends ABasicAdminDto {
    @ApiModelProperty(name = "street")
    private String street;
    @ApiModelProperty(name = "zipCode")
    private String zipCode;
    @ApiModelProperty(name = "isDefault")
    private Boolean isDefault;
    @ApiModelProperty(name = "userId")
    private Long userId;
    @ApiModelProperty(name = "province")
    private NationDto province;
    @ApiModelProperty(name = "district")
    private NationDto district;
    @ApiModelProperty(name = "commune")
    private NationDto commune;
}
