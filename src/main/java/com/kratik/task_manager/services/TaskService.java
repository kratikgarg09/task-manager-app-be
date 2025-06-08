package com.kratik.task_manager.services;

import com.kratik.task_manager.dto.TaskDto;
import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.TaskRepository;
import com.kratik.task_manager.repository.UserRepository;
import com.kratik.task_manager.utility.CommonFunctions;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final CommonFunctions commonFunctions;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, CommonFunctions commonFunctions) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commonFunctions = commonFunctions;
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
                .build();

        TasksEntity savedTask = taskRepository.save(task);

        return commonFunctions.getTaskResponseDto(savedTask,getCurrentUser());
    }

    public List<TaskResponseDTO> getTasksForCurrentUser() {

        List<TasksEntity> tasksEntities = taskRepository.findByUser(getCurrentUser());

        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            taskResponseDTOS.add(commonFunctions.getTaskResponseDto(tasks,getCurrentUser()));
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

        return commonFunctions.getTaskResponseDto(task,getCurrentUser());
    }


    public List<TasksEntity> getTodayTasks() {
        return taskRepository.findByUserAndDueDate(getCurrentUser(), LocalDate.now());
    }

    public List<TasksEntity> getCompletedTasks(boolean completed) {
        return taskRepository.findByUserAndCompleted(getCurrentUser(), completed);
    }

    public List<TasksEntity> getUpcomingTasks() {
        return taskRepository.findByUserAndDueDateAfter(getCurrentUser(), LocalDate.now());
    }

    public List<TasksEntity> searchTasks(String keyword) {
        return taskRepository.findByUserAndTitleContainingIgnoreCase(getCurrentUser(), keyword);
    }
}