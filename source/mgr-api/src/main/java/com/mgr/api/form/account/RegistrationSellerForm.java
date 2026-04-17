package com.mgr.api.form.account;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
public class RegistrationSellerForm {
    @NotEmpty(message = "username is required")
    @ApiModelProperty(name = "username", required = true)
    private String username;

    @NotEmpty(message = "password is required")
    @ApiModelProperty(name = "password", required = true)
    private String password;

    @NotEmpty(message = "fullName is required")
    @ApiModelProperty(name = "fullName", required = true)
    private String fullName;

    @Email(message = "invalid email format")
    @NotEmpty(message = "email is required")
    @ApiModelProperty(name = "email", required = true)
    private String email;

    @NotEmpty(message = "phone is required")
    @ApiModelProperty(name = "phone", required = true)
    private String phone;
}