package com.mgr.api.mapper;

import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.dto.news.NewsDto;
import com.mgr.api.form.news.CreateNewsForm;
import com.mgr.api.form.news.UpdateNewsForm;
import com.mgr.api.model.Category;
import com.mgr.api.model.News;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {CategoryMapper.class})
public interface NewsMapper {
    // Không ignore id và category, dùng source/target để ánh xạ thẳng
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
   // @Mapping(source = "categoryId", target = "category.id")
    News fromCreateNewsFormToEntity(CreateNewsForm form);

    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    //@Mapping(source = "categoryId", target = "category.id")
    void mappingUpdateNewsFormToEntity(UpdateNewsForm form, @MappingTarget News news);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "content", target = "content")
    // Sử dụng CategoryMapper để lấy đúng 3 trường: id, name, description
    @Mapping(source = "category", target = "category", qualifiedByName = "fromEntityToCategoryDtoShort")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToNewsDto")
    NewsDto fromEntityToNewsDto(News news);

    @IterableMapping(elementTargetType = NewsDto.class, qualifiedByName = "fromEntityToNewsDto")
    @Named("fromEntityToNewsDtoList")
    List<NewsDto> fromEntityToNewsDtoList(List<News> newsList);
}