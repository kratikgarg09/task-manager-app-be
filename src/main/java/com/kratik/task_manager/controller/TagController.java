package com.kratik.task_manager.controller;


import com.kratik.task_manager.model.CategoryEntity;
import com.kratik.task_manager.model.TagsEntity;
import com.kratik.task_manager.services.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagsService tagService;

    @GetMapping("/get-all-tags")
    public List<TagsEntity> getAll(){
        return tagService.getAllTags();
    }

}
