package com.mgr.api.form.user;


import com.mgr.api.dto.address.AddressDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateUserForm {
    @NotNull(message = "User ID is required")
    @ApiModelProperty(name = "id", required = true)
    private Long id;

    @ApiModelProperty(name = "fullName")
    private String fullName;

    @ApiModelProperty(name = "phone")
    private String phone;

    @ApiModelProperty(name = "gender")
    private Integer gender;

    @ApiModelProperty(name = "avatarPath")
    private String avatarPath;

    @ApiModelProperty(name = "Address")
    private List<AddressDto> addressDtoList;
}
