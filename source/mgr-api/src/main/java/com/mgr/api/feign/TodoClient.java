package com.mgr.api.feign;

import com.mgr.api.dto.ExternalTodoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "todoClient", url = "https://jsonplaceholder.typicode.com")
public interface TodoClient {

    @GetMapping("/todos/{id}")
    ExternalTodoDTO getTodoById(@PathVariable("id") Long id);
}


