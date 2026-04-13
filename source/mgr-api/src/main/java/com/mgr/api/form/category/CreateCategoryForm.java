package com.mgr.api.form.category;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@ApiModel
@Getter
@Setter
@NoArgsConstructor
public class CreateCategoryForm {
    @NotEmpty(message = "name is required")
    @ApiModelProperty(name = "name", required = true)
    private String name;

    @ApiModelProperty(name = "description")
    private String description;

    @ApiModelProperty(name = "parentId")
    private Long parentId;
}
