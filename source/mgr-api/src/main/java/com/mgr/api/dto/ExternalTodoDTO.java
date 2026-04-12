package com.mgr.api.dto;

import lombok.Data;

@Data
public class ExternalTodoDTO {
    private Long id;
    private String title;
    private boolean completed;
}
