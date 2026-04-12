package com.mgr.api.dto.news;

import com.mgr.api.dto.ABasicAdminDto;
import com.mgr.api.dto.category.CategoryDto;
import lombok.Data;

@Data
public class NewsDto extends ABasicAdminDto {
    private String title;
    private String content;
    private CategoryDto category;
}