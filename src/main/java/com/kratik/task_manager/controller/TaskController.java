package com.kratik.task_manager.controller;

import com.kratik.task_manager.dto.TaskDto;
import com.kratik.task_manager.dto.TaskResponseDTO;
import com.kratik.task_manager.model.Priority;
import com.kratik.task_manager.model.TasksEntity;
import com.kratik.task_manager.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        List<TaskResponseDTO> tasks = taskService.getTasksForCurrentUser();

        data.put("total", tasks.size());
        data.put("completed", tasks.stream().filter(t -> t.getStatus().equals("COMPLETED")).count());
        data.put("pending", tasks.stream().filter(t -> t.getStatus().equals("PENDING")).count());
        data.put("overdue", tasks.stream().filter(t -> t.getDueDate().isBefore(LocalDate.now())).count());
        data.put("dueToday", tasks.stream().filter(t -> t.getDueDate().isEqual(LocalDate.now())).count());

        return ResponseEntity.ok(data);
    }

    @GetMapping("/search-task-filter")
    public ResponseEntity<List<TaskResponseDTO>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
    ) {
        List<TaskResponseDTO> results = taskService.searchTasksByParameter( title, status, category, tag, fromDate,
                toDate);
        return ResponseEntity.ok(results);
    }
}