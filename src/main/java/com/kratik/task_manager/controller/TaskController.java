package com.kratik.task_manager.controller;

import com.kratik.task_manager.dto.TaskDto;
import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public TaskResponseDTO create(@RequestBody @Valid TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @GetMapping("/get-all")
    public List<TaskResponseDTO> getAll() {
        return taskService.getTasksForCurrentUser();
    }

    @PutMapping("/{id}")
    public TaskResponseDTO update(@PathVariable Long id, @RequestBody @Valid TaskDto taskDto) {
        return taskService.updateTask(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }


    @GetMapping("/today")
    public List<TaskResponseDTO> todayTasks() {
        return taskService.getTodayTasks();
    }

    @GetMapping("/completed")
    public List<TaskResponseDTO> completedTasks(@RequestParam boolean status) {
        return taskService.getCompletedTasks(status);
    }

    @GetMapping("/upcoming")
    public List<TaskResponseDTO> upcomingTasks() {
        return taskService.getUpcomingTasks();
    }

    @GetMapping("/search")
    public List<TaskResponseDTO> search(@RequestParam String keyword) {
        return taskService.searchTasks(keyword);
    }

    @GetMapping("/priority")
    public List<TaskResponseDTO> getByPriority(@RequestParam Priority level) {
        return taskService.getByPriority(level);
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTask(@PathVariable Long id){
        return taskService.getTask(id);
    }
}