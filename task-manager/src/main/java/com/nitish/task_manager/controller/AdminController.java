package com.nitish.task_manager.controller;

import com.nitish.task_manager.dto.*;
import com.nitish.task_manager.exception.ResourceNotFoundException;
import com.nitish.task_manager.model.Task;
import com.nitish.task_manager.service.TaskService;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final TaskService taskService;

    public AdminController(TaskService taskService) {
        this.taskService = taskService;
    }

//    @GetMapping("/test")
//    public ResponseEntity<ApiResponse<String>> test() {
//        return ResponseEntity.ok(
//                new ApiResponse<>(true, "Admin API working", null)
//        );
//    }

    @PostMapping("/task/create")
    public ResponseEntity<ApiResponse<String>> createTask(@RequestBody TaskCreateDTO dto) {

        boolean hasEmployee = dto.getAssignedTo() != null && !dto.getAssignedTo().isEmpty();
        boolean hasManager = dto.getAssignedManager() != null && !dto.getAssignedManager().isEmpty();

        if (!hasEmployee && !hasManager) {
            throw new RuntimeException("Assign task to employee or manager (or both)");
        }

        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());


        if (!hasManager && hasEmployee) {
            task.setAssignedManager(dto.getAssignedTo());
        }

        task.setCreatedBy("ADMIN");
        task.setStatus("PENDING");

        taskService.save(task);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Task created by Admin", null)
        );
    }

    @GetMapping("/tasks")
    public ResponseEntity<ApiResponse<List<TaskResponseDTO>>> getAllTasks() {

        List<Task> tasks = taskService.getAllTasks();

        List<TaskResponseDTO> response = tasks.stream().map(task -> {
            TaskResponseDTO dto = new TaskResponseDTO();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setAssignedTo(task.getAssignedTo());
            dto.setStatus(task.getStatus());
            return dto;
        }).toList();

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Tasks fetched successfully", response)
        );
    }

    @PutMapping("/task/{id}")
    public ResponseEntity<ApiResponse<String>> updateAnyTask(@PathVariable String id,
                                                             @RequestBody TaskUpdateDTO dto) {

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ID format");
        }

        Task task = taskService.getById(id);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }

        if (dto.getStatus() == null || dto.getStatus().isEmpty()) {
            throw new RuntimeException("Status is required");
        }
        task.setStatus(dto.getStatus());
        taskService.save(task);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Task updated successfully", null)
        );
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAnyTask(@PathVariable String id) {

        if (!ObjectId.isValid(id)) {
            throw new IllegalArgumentException("Invalid ID format");
        }

        Task task = taskService.getById(id);

        if (task == null) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskService.delete(task);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Task deleted successfully", null)
        );
    }
}