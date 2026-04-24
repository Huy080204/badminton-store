package com.mgr.api.form.news;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateNewsForm {
    @NotNull(message = "Id cannot be null")
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
    private Integer status;
}