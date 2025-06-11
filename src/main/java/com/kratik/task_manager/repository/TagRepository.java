package com.kratik.task_manager.repository;

import com.kratik.task_manager.model.TagsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository <TagsEntity,Long> {
    Optional<TagsEntity> findById(Long id);
}
