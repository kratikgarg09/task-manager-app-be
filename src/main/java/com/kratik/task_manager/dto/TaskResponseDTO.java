package com.kratik.task_manager.dto;

import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private Priority priority;
    private boolean completed;
    private LocalDateTime reminderTime;
    private TaskStatus status;
    private Long categoryId;  // for assigning category by id
    private Set<Long> tagIds;
    private UserDTO user;
}