package com.mgr.api.controller;

import com.mgr.api.dto.ApiResponse;
import com.mgr.api.dto.ErrorCode;
import com.mgr.api.dto.ResponseListDto;
import com.mgr.api.dto.news.NewsDto;
import com.mgr.api.exception.NotFoundException;
import com.mgr.api.form.news.CreateNewsForm;
import com.mgr.api.form.news.UpdateNewsForm;
import com.mgr.api.mapper.NewsMapper;
import com.mgr.api.model.Category;
import com.mgr.api.model.News;
import com.mgr.api.model.criteria.NewsCriteria;
import com.mgr.api.repository.CategoryRepository;
import com.mgr.api.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController("newsControllerV2")
@RequestMapping("/v1/news")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class NewsController extends ABasicController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private NewsMapper newsMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_C')")
    @Transactional
    public ApiResponse<String> create(@Valid @RequestBody CreateNewsForm createNewsForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        Category category = categoryRepository.findById(createNewsForm.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found!", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        News news = newsMapper.fromCreateNewsFormToEntity(createNewsForm);
        news.setCategory(category);
        news.setStatus(createNewsForm.getStatus());

        newsRepository.save(news);
        apiMessageDto.setMessage("Create news success.");
        return apiMessageDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_U')")
    @Transactional
    public ApiResponse<String> update(@Valid @RequestBody UpdateNewsForm updateNewsForm, BindingResult bindingResult) {
        ApiResponse<String> apiMessageDto = new ApiResponse<>();
        News news = newsRepository.findById(updateNewsForm.getId())
                .orElseThrow(() -> new NotFoundException("News not found!", ErrorCode.NEWS_ERROR_NOT_FOUND));

        // categoryID has been valid in form
        Category category = categoryRepository.findById(updateNewsForm.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found!", ErrorCode.CATEGORY_ERROR_NOT_FOUND));

        news.setCategory(category);

        newsMapper.mappingUpdateNewsFormToEntity(updateNewsForm, news);
        newsRepository.save(news);
        apiMessageDto.setMessage("Update news success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_V')")
    public ApiResponse<NewsDto> get(@PathVariable("id") Long id) {
        ApiResponse<NewsDto> apiMessageDto = new ApiResponse<>();
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("News not found!", ErrorCode.NEWS_ERROR_NOT_FOUND));

        apiMessageDto.setData(newsMapper.fromEntityToNewsDto(news));
        apiMessageDto.setMessage("Get news success.");
        return apiMessageDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_L')")
    public ApiResponse<ResponseListDto<List<NewsDto>>> list(NewsCriteria newsCriteria, Pageable pageable) {
        // Sửa kiểu trả về của ApiResponse thành ResponseListDto<List<NewsDto>>
        ApiResponse<ResponseListDto<List<NewsDto>>> apiMessageDto = new ApiResponse<>();

        // Lấy dữ liệu phân trang từ repository
        Page<News> page = newsRepository.findAll(newsCriteria.getSpecification(), pageable);

        // newsMapper.fromEntityListToDtoList trả về List<NewsDto>
        // Vì vậy T trong ResponseListDto là List<NewsDto>
        ResponseListDto<List<NewsDto>> responseListDto = new ResponseListDto<>(
                newsMapper.fromEntityToNewsDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        apiMessageDto.setData(responseListDto);
        apiMessageDto.setMessage("List news success.");
        return apiMessageDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_D')")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("News not found!", ErrorCode.NEWS_ERROR_NOT_FOUND));

        newsRepository.delete(news);
        ApiResponse<Void> response = new ApiResponse<>();
        response.setMessage("Delete news success");
        return response;
    }


}