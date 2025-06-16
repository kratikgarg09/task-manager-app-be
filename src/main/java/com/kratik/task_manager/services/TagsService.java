package com.kratik.task_manager.services;

import com.kratik.task_manager.model.TagsEntity;
import com.kratik.task_manager.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsService {

    private final TagRepository tagRepository;
    public List<TagsEntity> getAllTags() {
       return tagRepository.findAll();
    }
}
