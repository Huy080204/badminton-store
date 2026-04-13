package com.mgr.api.dto.user;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    //ABasic
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "status")
    private Integer status;
    @ApiModelProperty(name = "phone")
    private String phone;
    @ApiModelProperty(name = "email")
    private String email;
    @ApiModelProperty(name = "fullName")
    private String fullName;
    @ApiModelProperty(name = "lastLogin")
    private Date lastLogin;
    @ApiModelProperty(name = "avatar")
    private String avatar;
    @ApiModelProperty(name = "gender")
    private Integer gender;
//    @ApiModelProperty(name = "address")
//    private List<AddressDto> address;

}