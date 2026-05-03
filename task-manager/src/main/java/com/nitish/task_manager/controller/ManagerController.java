package com.nitish.task_manager.controller;

import com.nitish.task_manager.dto.TaskCreateDTO;
import com.nitish.task_manager.dto.TaskResponseDTO;
import com.nitish.task_manager.exception.ResourceNotFoundException;
import com.nitish.task_manager.model.Task;
import com.nitish.task_manager.service.TaskService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

    private final TaskService taskService;

    public ManagerController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/test")
    public String test() {
        return "Manager OK";
    }

    @PostMapping("/task/create")
    public ResponseEntity<String> createTask(@RequestBody TaskCreateDTO dto,
                                             Authentication authentication) {

        String managerEmail = authentication.getName();

        if (dto.getAssignedTo() == null || dto.getAssignedTo().isEmpty()) {
            throw new RuntimeException("Assigned employee is required");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setAssignedTo(dto.getAssignedTo());

        task.setCreatedBy(managerEmail);
        task.setAssignedManager(managerEmail);
        task.setStatus("PENDING");

        taskService.save(task);

        return ResponseEntity.ok("Task created successfully");
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getManagerTasks(Authentication authentication) {

        String managerEmail = authentication.getName();

        List<Task> tasks = taskService.getTasksByManager(managerEmail);

        List<TaskResponseDTO> response = tasks.stream().map(task -> {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAssignedTo(task.getAssignedTo());
            dto.setStatus(task.getStatus());
            return dto;
        }).toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable String id,
                                             Authentication authentication) {

        String managerEmail = authentication.getName();

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ID format");
        }

        Task task = taskService.getById(id);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }

        if ("ADMIN".equals(task.getCreatedBy())) {
            throw new RuntimeException("You cannot delete admin-created tasks");
        }

        if (task.getAssignedManager() == null ||
                !task.getAssignedManager().equals(managerEmail)) {
            throw new RuntimeException("You can only delete your own employee tasks");
        }

        taskService.delete(task);

        return ResponseEntity.ok("Task deleted by Manager");
    }
}