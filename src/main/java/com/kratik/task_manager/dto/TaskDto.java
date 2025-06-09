package com.kratik.task_manager.dto;

import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.model.TaskStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class TaskDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    @NotNull(message = "Priority is required")
    private Priority priority;

    private LocalDateTime reminderTime;
    private boolean completed;

    private TaskStatus status;

    private Long categoryId;  // for assigning category by id
    private Set<Long> tagIds;

}
