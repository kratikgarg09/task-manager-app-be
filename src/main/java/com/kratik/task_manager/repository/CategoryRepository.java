package com.kratik.task_manager.repository;

import com.kratik.task_manager.model.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Long, CategoryEntity> {
    Optional<CategoryEntity> findById (Long id);
}
