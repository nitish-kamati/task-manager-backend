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

    // 🔥 get tasks by employee
    public List<Task> getTasksByEmployee(String email) {
        return taskRepository.findByAssignedTo(email);
    }

    // 🔥 get tasks by manager
    public List<Task> getTasksByManager(String email) {
        return taskRepository.findByAssignedManager(email);
    }

    // 🔥 get all tasks (admin)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // 🔥 save task
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    // 🔥 get by id
    public Task getById(String id) {
        return taskRepository.findById(id).orElse(null);
    }

    // 🔥 delete
    public void delete(Task task) {
        taskRepository.delete(task);
    }
}