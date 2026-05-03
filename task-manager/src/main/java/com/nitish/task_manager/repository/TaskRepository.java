package com.nitish.task_manager.repository;

import com.nitish.task_manager.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {

    List<Task> findByAssignedTo(String email);

    List<Task> findByAssignedManager(String email);

    List<Task> findByCreatedBy(String email);
}