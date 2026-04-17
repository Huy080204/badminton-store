package com.mgr.api.form.nation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateNationForm {
    @ApiModelProperty(name = "id", required = true)
    @NotNull(message = "Id cannot be null")
    private Long id;
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
