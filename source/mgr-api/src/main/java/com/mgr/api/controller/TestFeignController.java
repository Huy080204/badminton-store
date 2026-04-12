package com.mgr.api.controller;

import com.mgr.api.dto.ExternalTodoDTO;
import com.mgr.api.feign.TodoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-external")
public class TestFeignController {
    @Autowired
    private TodoClient todoClient;
    @GetMapping("/todo/{id}")
    public ResponseEntity<ExternalTodoDTO> testGetTodo(@PathVariable Long id) {
        // Gọi API bên thứ ba thông qua Feign
        ExternalTodoDTO response = todoClient.getTodoById(id);
        return ResponseEntity.ok(response);
    }
}
