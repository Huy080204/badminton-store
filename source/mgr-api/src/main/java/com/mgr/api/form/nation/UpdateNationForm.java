package com.mgr.api.form.nation;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateNationForm {
    @NotNull(message = "Id cannot be null")
    private Long id;
    private String name;
    private Integer kind;
    private Long parentId;
}
