package com.kratik.task_manager.services;

import com.kratik.task_manager.model.CategoryEntity;
import com.kratik.task_manager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public List<CategoryEntity> getAllCategory() {
        return categoryRepository.findAll();
    }
}
