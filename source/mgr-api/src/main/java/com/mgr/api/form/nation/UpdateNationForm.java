package com.mgr.api.form.nation;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateNationForm {
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
