package com.kratik.task_manager.services;

import com.kratik.task_manager.dto.TaskDto;
import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.dto.UserDTO;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.model.UserEntity;
import com.kratik.task_manager.repository.TaskRepository;
import com.kratik.task_manager.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    private UserEntity getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("UserEntity not found"));
    }

    public TaskResponseDTO createTask(TaskDto taskDto) {
        UserEntity user = getCurrentUser();

        TasksEntity task = TasksEntity.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .dueDate(taskDto.getDueDate())
                .completed(taskDto.isCompleted())
                .user(user)
                .build();

        TasksEntity savedTask = taskRepository.save(task);
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmailId(),
                user.getMobileNumber()
        );
        return new TaskResponseDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getDueDate(),
                savedTask.isCompleted(),
                userDTO
        );
    }

    public List<TaskResponseDTO> getTasksForCurrentUser() {
        UserEntity user = getCurrentUser();

        List<TasksEntity> tasksEntities = taskRepository.findByUser(user);

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmailId(),
                user.getMobileNumber()
        );

        List<TaskResponseDTO> taskResponseDTOS = new ArrayList<>();
        for (TasksEntity tasks : tasksEntities){
            TaskResponseDTO taskResponseDTO = new TaskResponseDTO(
                    tasks.getId(),
                    tasks.getTitle(),
                    tasks.getDescription(),
                    tasks.getDueDate(),
                    tasks.isCompleted(),
                    userDTO
            );
            taskResponseDTOS.add(taskResponseDTO);
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

        UserEntity user = getCurrentUser();

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmailId(),
                user.getMobileNumber()
        );
        return new TaskResponseDTO( task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.isCompleted(),
                userDTO);
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