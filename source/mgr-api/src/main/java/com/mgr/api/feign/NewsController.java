package com.mgr.api.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgr.api.controller.ABasicController;
import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.category.CategoryDto;
import com.mgr.api.dto.news.NewsDto;
import com.mgr.api.form.news.CreateNewsForm;
import com.mgr.api.form.news.UpdateNewsForm;
import com.mgr.api.mapper.NewsMapper;
import com.mgr.api.model.Account;
import com.mgr.api.model.Category;
import com.mgr.api.model.News;
import com.mgr.api.repository.NewsRepository;
import com.mgr.api.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/v2/news")
@Slf4j
public class NewsController extends ABasicController {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private CategoryInternalClient categoryInternalClient;

    @Autowired
    private NewsMapper newsMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateNewsForm createNewsForm, BindingResult bindingResult) {
        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();

        // 0. Check Validation
        if (bindingResult.hasErrors()) {
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Validation failed!");
            return apiMessageDto;
        }

        try {
            // 1. Gọi Feign với kiểu Object để tránh lỗi Converter
            ApiMessageDto<Object> categoryResponse = categoryInternalClient.getCategoriesInternal();

            if (categoryResponse == null || categoryResponse.getData() == null) {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Category Service error.");
                return apiMessageDto;
            }

            // 2. Ép kiểu dữ liệu bằng ObjectMapper (Đây là tuyệt chiêu né lỗi Type)
            ObjectMapper mapper = new ObjectMapper();
            List<CategoryDto> categories = mapper.convertValue(
                    categoryResponse.getData(),
                    new TypeReference<List<CategoryDto>>() {}
            );

            long targetId = createNewsForm.getCategoryId();

            boolean exists = categories.stream()
                    .anyMatch(cat -> {
                        // Ép mọi thứ về String để so sánh cho chắc chắn không lệch kiểu
                        String idFromFeign = String.valueOf(cat.getId());
                        String idFromPostman = String.valueOf(targetId);
                        return idFromFeign.equals(idFromPostman);
                    });
            log.info("So sanh ID: {} voi danh sach Feign -> Kết quả: {}", targetId, exists);

            if (!exists) {
                apiMessageDto.setResult(false);
                apiMessageDto.setMessage("Category ID not found!");
                return apiMessageDto;
            }

            // 4. Khởi tạo News
            News news = new News();
            news.setTitle(createNewsForm.getTitle());
            news.setContent(createNewsForm.getContent());
            news.setStatus(1);

//            newsRepository.save(news);
//
//            apiMessageDto.setMessage("Create news success!");
//            return apiMessageDto;

            // THÊM: Gán Account cho News
            Account account = new Account();
            account.setId(getCurrentUser()); // Lấy ID từ token thông qua ABasicController
            news.setAccount(account);

            // THÊM: Gán Category nếu exists
            Category category = new Category();
            category.setId(createNewsForm.getCategoryId());
            news.setCategory(category);
            newsRepository.save(news);

            return makeSuccessResponse("Create news success!");

        } catch (Exception e) {
            log.error("Error: ", e);
            apiMessageDto.setResult(false);
            apiMessageDto.setMessage("Error: " + e.getMessage());
            return apiMessageDto;
        }
    }
    // UPDATE: Check chủ sở hữu
    @PutMapping(value = "/update/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_U')")
    public ApiMessageDto<NewsDto> update(@PathVariable Long id, @Valid @RequestBody UpdateNewsForm form) {
        // 1. Tìm News
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));

        // 2. Check quyền chủ sở hữu (CreatedBy là String)
        String currentUserId = SecurityUtils.getCurrentUserId().toString();
        if (news.getCreatedBy() == null || !news.getCreatedBy().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner!");
        }

        // 3. Dùng hàm mapping update của bạn
        newsMapper.mappingUpdateNewsFormToEntity(form, news);

        // Cập nhật lại Category nếu cần
        if (form.getCategoryId() != null) {
            Category category = new Category();
            category.setId(form.getCategoryId());
            news.setCategory(category);
        }

        newsRepository.save(news);

        ApiMessageDto<NewsDto> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setData(newsMapper.fromEntityToNewsDto(news));
        apiMessageDto.setMessage("Update news success.");
        return apiMessageDto;
    }

    // DELETE: Check chủ sở hữu
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('NEWS_D')")
    public ApiMessageDto<String> delete(@PathVariable Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found"));

        String currentUserId = SecurityUtils.getCurrentUserId().toString();
        if (news.getCreatedBy() == null || !news.getCreatedBy().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the owner!");
        }

        newsRepository.delete(news);

        ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
        apiMessageDto.setMessage("Delete news success.");
        return apiMessageDto;
    }
}