package com.mgr.api.form.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class CreateUserForm {
    @NotEmpty(message = "Username is required")
    @Size(min = 4, max = 50, message = "Username must be between 4 and 50 characters")
    @ApiModelProperty(name = "username", required = true)
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @ApiModelProperty(name = "password", required = true)
    private String password;

    @NotEmpty(message = "Full name is required")
    @ApiModelProperty(name = "fullName", required = true)
    private String fullName;

    @NotEmpty(message = "Email is required")
    @ApiModelProperty(name = "email", required = true)
    private String email;

    @NotEmpty(message = "Phone is required")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;

    @NotNull(message = "Gender is required")
    @ApiModelProperty(name = "gender", required = true)
    private Integer gender;

    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;

    @NotNull(message = "Group ID is required")
    @ApiModelProperty(name = "groupId", required = true)
    private Long groupId;
}
