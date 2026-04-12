package com.mgr.api.form.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ApiModel
public class UpdateUserForm {
    @NotNull(message = "id cannot be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "password")
    private String password;

    @ApiModelProperty(name = "fullName")
    private String fullName;

    @ApiModelProperty(name = "email")
    private String email;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;

    @ApiModelProperty(name = "gender")
    private Integer gender;

    @ApiModelProperty(name = "dateOfBirth")
    private Date dateOfBirth;

    @ApiModelProperty(name = "status")
    private Integer status;
}