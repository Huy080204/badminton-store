package com.mgr.api.form.news;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class CreateNewsForm {
    @NotEmpty(message = "Title cannot be empty")
    private String title;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    @NotNull(message = "CategoryId cannot be null")
    private Long categoryId;
    private Integer status;

}
