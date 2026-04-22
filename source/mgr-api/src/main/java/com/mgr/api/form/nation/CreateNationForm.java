package com.mgr.api.form.nation;

import com.mgr.api.validation.NationKind;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateNationForm {
    @ApiModelProperty(name = "kind", notes = "1: Province, 2: District, 3: Commune", allowableValues = "range[1,3]")
    @NotNull(message = "Kind cannot be null")
    @NationKind
    private Integer kind; // 1: Province, 2: District, 3: Commune
    @ApiModelProperty(name = "name")
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    @ApiModelProperty(name = "parentId")
    private Long parentId; // ID of father
}
