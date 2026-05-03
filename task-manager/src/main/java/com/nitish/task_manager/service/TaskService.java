package com.nitish.task_manager.service;

import com.nitish.task_manager.model.Task;
import com.nitish.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getTasksByEmployee(String email) {
        return taskRepository.findByAssignedTo(email);
    }

    public List<Task> getTasksByManager(String email) {
        return taskRepository.findByAssignedManager(email);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public Task getById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}