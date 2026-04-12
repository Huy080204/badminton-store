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
    @BeanMapping(ignoreByDefault = true)
    Category fromCreateCategoryFormToEntity(CreateCategoryForm form);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    void mappingUpdateCategoryToEntity(UpdateCategoryForm form, @MappingTarget Category category);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromCategoryToDto")
    CategoryDto fromCategoryToDto(Category category);

    /**
     * Chuyển đổi danh sách Entity sang danh sách DTO
     */
    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "fromCategoryToDto")
    @Named("fromEntityToCategoryDtoList")
    List<CategoryDto> fromEntityToCategoryDtoList(List<Category> categories);


    @Mapping(source = "name", target = "name")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "description", target = "description")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToCategoryDtoShort")
    CategoryDto fromEntityToCategoryDtoShort(Category category);
}
