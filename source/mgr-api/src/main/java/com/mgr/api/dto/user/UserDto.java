package com.mgr.api.dto.user;

import com.mgr.api.dto.ABasicAdminDto;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto extends ABasicAdminDto {
    private String username;
    private String fullName;
    private String email;
    private Integer gender;
    private Date dateOfBirth;
    private String avatarPath;
}