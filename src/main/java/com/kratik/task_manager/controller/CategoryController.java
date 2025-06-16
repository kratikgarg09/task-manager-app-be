package com.kratik.task_manager.controller;

import com.kratik.task_manager.model.CategoryEntity;
import com.kratik.task_manager.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/get-all-categories")
    public List<CategoryEntity> getAll(){
        return categoryService.getAllCategory();
    }

}
