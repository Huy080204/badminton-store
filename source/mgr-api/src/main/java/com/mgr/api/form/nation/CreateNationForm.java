package com.mgr.api.form.nation;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateNationForm {
    @NotNull(message = "Kind cannot be null")
    private Integer kind; // 1: Province, 2: District, 3: Commune
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    private Long parentId; // ID of father
}
