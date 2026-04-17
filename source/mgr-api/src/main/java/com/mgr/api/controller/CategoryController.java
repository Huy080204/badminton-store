package com.mgr.api.controller;
import com.mgr.api.constant.MgrConstant;
import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.category.CreateCategoryForm;
import com.mgr.api.form.category.UpdateCategoryForm;
import com.mgr.api.mapper.CategoryMapper;
import com.mgr.api.model.Category;
import com.mgr.api.model.criteria.CategoryCriteria;
import com.mgr.api.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController extends ABasicController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_C')")
    @Transactional
    public ApiMessageDto<Void> create(@Valid @RequestBody CreateCategoryForm createCategoryForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return makeResponse(false, null, "Invalid form", ErrorCode.ERROR_INVALID_FORM);
        }
        Category category = categoryMapper.fromCreateFormToEntity(createCategoryForm);
        category.setStatus(MgrConstant.STATUS_ACTIVE);

        if (createCategoryForm.getParentId() != null) {
            Category parent = categoryRepository.findById(createCategoryForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));
            category.setParent(parent);
        }

        categoryRepository.save(category);
        return makeSuccessResponse(null, "Create Category success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_U')")
    @Transactional
    public ApiMessageDto<Void> update(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return makeResponse(false, null, "Invalid form", ErrorCode.ERROR_INVALID_FORM);
        }
        Category category = categoryRepository.findById(updateCategoryForm.getId())
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        categoryMapper.mappingUpdateFormToEntity(updateCategoryForm, category);
        if (updateCategoryForm.getStatus() != null) {
            category.setStatus(updateCategoryForm.getStatus());
        }

        if (updateCategoryForm.getParentId() != null) {
            Category parent = categoryRepository.findById(updateCategoryForm.getParentId())
                    .orElseThrow(() -> new NotFoundException("Parent category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));
            category.setParent(parent);
        }

        categoryRepository.save(category);
        return makeSuccessResponse(null, "Update Category success");
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<CategoryDto> get(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));
        return makeSuccessResponse(categoryMapper.fromEntityToDto(category), "Get Category success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<CategoryDto>>> list(CategoryCriteria categoryCriteria, Pageable pageable) {
        Page<Category> page = categoryRepository.findAll(categoryCriteria.getSpecification(), pageable);
        ResponseListDto<List<CategoryDto>> listDto = new ResponseListDto<>();

        listDto.setContent(categoryMapper.fromEntityListToDtoList(page.getContent()));
        listDto.setTotalElements(page.getTotalElements());
        listDto.setTotalPages(page.getTotalPages());

        return makeSuccessResponse(listDto, "Get list category success");
    }
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('CAT_D')")
    @Transactional
    public ApiMessageDto<Void> delete(@PathVariable("id") Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found", ErrorCode.CATEGORY_ERROR_NOT_FOUND));
        category.setStatus(MgrConstant.STATUS_DELETE);
        categoryRepository.save(category);
        return makeSuccessResponse(null, "Delete Category success");
    }
}
