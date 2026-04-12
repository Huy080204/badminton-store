package com.mgr.api.feign;

import com.mgr.api.dto.ApiMessageDto;
import com.mgr.api.dto.category.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

// name: Tên của service (để Spring quản lý)
// url: Trỏ thẳng về địa chỉ của CategoryController
@FeignClient(
        name = "category-internal",
        url = "http://localhost:8787/v1/category",
        configuration = FeignConfig.class // THÊM DÒNG NÀY
)
public interface CategoryInternalClient {

    @GetMapping(value = "/list-internal")
    ApiMessageDto<Object> getCategoriesInternal();
}