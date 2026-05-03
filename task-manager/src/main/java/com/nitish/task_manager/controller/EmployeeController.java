package com.nitish.task_manager.controller;

import com.nitish.task_manager.dto.TaskResponseDTO;
import com.nitish.task_manager.dto.TaskUpdateDTO;
import com.nitish.task_manager.exception.ResourceNotFoundException;
import com.nitish.task_manager.model.Task;
import com.nitish.task_manager.service.TaskService;
import org.springframework.security.core.Authentication;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final TaskService taskService;

    public EmployeeController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getMyTasks(Authentication authentication) {

        String email = authentication.getName();

        List<Task> tasks = taskService.getTasksByEmployee(email);

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

    @PutMapping("/task/{id}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable String id,
                                                   @RequestBody TaskUpdateDTO dto,
                                                   Authentication authentication) {

        String email = authentication.getName();

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ID format");
        }

        Task task = taskService.getById(id);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (!task.getAssignedTo().equals(email)) {
            throw new RuntimeException("You can only update your own task");
        }

        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            throw new RuntimeException("Status is required");
        }

        task.setStatus(dto.getStatus());

        taskService.save(task);

        return ResponseEntity.ok("Task updated successfully");
    }
}