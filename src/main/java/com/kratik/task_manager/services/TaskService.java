package com.kratik.task_manager.services;

import com.kratik.task_manager.dto.TaskDto;
import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.model.*;
import com.kratik.task_manager.repository.CategoryRepository;
import com.kratik.task_manager.repository.TagRepository;
import com.kratik.task_manager.repository.TaskRepository;
import com.kratik.task_manager.repository.UserRepository;
import com.kratik.task_manager.utility.CommonFunctions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final CommonFunctions commonFunctions;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CommonFunctions commonFunctions, TagRepository tagRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commonFunctions = commonFunctions;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found"));
    }

    public TaskResponseDTO createTask(TaskDto taskDto) {

        TasksEntity task = TasksEntity.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .completed(taskDto.isCompleted())
                .user(getCurrentUser())
                .priority(taskDto.getPriority())
                .reminderTime(taskDto.getReminderTime())
                .build();

        CategoryEntity category = categoryRepository.findById(taskDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Set<TagsEntity> tags = new HashSet<>();
        for (Long tagId : taskDto.getTagIds()) {
            TagsEntity tag = tagRepository.findById(tagId)
                    .orElseThrow(() -> new RuntimeException("Tag not found: " + tagId));
            tags.add(tag);
        }
        task.setCategory(category);
        task.setTags(tags);

        TasksEntity savedTask = taskRepository.save(task);

        return commonFunctions.getTaskResponseDto(savedTask);
    }

    public List<TaskResponseDTO> getTasksForCurrentUser() {

        List<TasksEntity> tasksEntities = taskRepository.findByUser(getCurrentUser());

        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }

    public void deleteTask(Long id) {
        TasksEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TasksEntity not found"));
        if (!task.getUser().equals(getCurrentUser())) {
            throw new RuntimeException("Unauthorized");
        }
        taskRepository.delete(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskDto taskDto) {
        TasksEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TasksEntity not found"));
        if (!task.getUser().equals(getCurrentUser())) {
            throw new RuntimeException("Unauthorized");
        }

        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setDueDate(taskDto.getDueDate());
        task.setCompleted(taskDto.isCompleted());
        taskRepository.save(task);

        return commonFunctions.getTaskResponseDto(task);
    }


    public List<TaskResponseDTO> getTodayTasks() {
        List<TasksEntity> tasksEntities = taskRepository.findByUserAndDueDate(getCurrentUser(), LocalDate.now());
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }

    public List<TaskResponseDTO> getCompletedTasks(boolean completed) {
        List<TasksEntity> tasksEntities = taskRepository.findByUserAndCompleted(getCurrentUser(), completed);
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }

    public List<TaskResponseDTO> getUpcomingTasks() {
        List<TasksEntity> tasksEntities =  taskRepository.findByUserAndDueDateAfter(getCurrentUser(), LocalDate.now());
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }

    public List<TaskResponseDTO> searchTasks(String keyword) {
        List<TasksEntity> tasksEntities =  taskRepository.findByUserAndTitleContainingIgnoreCase(getCurrentUser(), keyword);
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }

    public List<TaskResponseDTO> getByPriority(Priority priority) {
        List<TasksEntity> tasksEntities =  taskRepository.findByUserAndPriority(getCurrentUser(), priority);
        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks));
        }
        return taskResponseDTOS;
    }
}