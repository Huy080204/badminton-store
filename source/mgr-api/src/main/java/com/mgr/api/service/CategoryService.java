package com.mgr.api.service;

import com.mgr.api.dto.ErrorCode;
import com.mgr.api.exception.BadRequestException;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.category.CreateCategoryForm;
import com.mgr.api.form.category.UpdateCategoryForm;
import com.mgr.api.mapper.CategoryMapper;
import com.mgr.api.model.Category;
import com.mgr.api.repository.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Transactional
    public void createCategory(CreateCategoryForm form) {
        Category category = categoryRepository.findFirstByName(form.getName());
        if (category != null) {
            throw new BadRequestException("Category name already exists!", ErrorCode.CATEGORY_ERROR_NAME_EXISTED);
        }
        category = categoryMapper.fromCreateCategoryFormToEntity(form);
        categoryRepository.save(category);
    }

    @Transactional
    public void updateCategory(UpdateCategoryForm form) {
        Category category = categoryRepository.findById(form.getId())
                .orElseThrow(() -> new NotFoundException("Category not found!", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        if (StringUtils.isNoneBlank(form.getName())) {
            Category otherCategory = categoryRepository.findFirstByName(form.getName());
            if (otherCategory != null && !otherCategory.getId().equals(form.getId())) {
                throw new BadRequestException("Category name already exists!", ErrorCode.CATEGORY_ERROR_NAME_EXISTED);
            }
        }

        categoryMapper.mappingUpdateCategoryToEntity(form, category);
        categoryRepository.save(category);
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found!", ErrorCode.CATEGORY_ERROR_NOT_FOUND));
    }
}