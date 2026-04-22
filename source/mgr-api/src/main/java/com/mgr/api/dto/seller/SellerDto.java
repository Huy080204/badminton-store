package com.mgr.api.dto.seller;

import com.mgr.api.dto.ABasicAdminDto;
import com.mgr.api.dto.account.AccountDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SellerDto extends ABasicAdminDto {
    @ApiModelProperty(name = "shopName")
    private String shopName;

    @ApiModelProperty(name = "shopDescription")
    private String shopDescription;

    @ApiModelProperty(name = "account")
    private AccountDto account;
}