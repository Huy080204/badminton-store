package com.mgr.api.mapper;

import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.form.category.CreateCategoryForm;
import com.mgr.api.form.category.UpdateCategoryForm;
import com.mgr.api.model.Category;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "parent", ignore = true)
    Category fromCreateFormToEntity(CreateCategoryForm form);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "parent", ignore = true)
    void mappingUpdateFormToEntity(UpdateCategoryForm form, @MappingTarget Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "status", target = "status")
    CategoryDto fromEntityToDto(Category category);

    @IterableMapping(elementTargetType = CategoryDto.class)
    List<CategoryDto> fromEntityListToDtoList(List<Category> list);
}
