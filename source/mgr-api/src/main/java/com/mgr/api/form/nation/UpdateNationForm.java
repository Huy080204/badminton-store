package com.mgr.api.form.nation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class UpdateNationForm {
    @NotNull(message = "Kind cannot be null")
    private Long id;
    private String name;
    private Long parentId;
}
