package com.mgr.api.form.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
public class CreateUserForm {
    @NotEmpty(message = "Username cannot be empty")
    private String username;
    @NotEmpty(message = "Password cannot be empty")
    private String password;
    @NotEmpty(message = "Full name cannot be empty")
    private String fullName;
    private String email;
    private Integer gender;
    private Date dateOfBirth;
    private String phone;
}
