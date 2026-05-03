package com.nitish.task_manager.dto;

import lombok.Data;

@Data
public class TaskCreateDTO {
    private String title;
    private String description;
    private String assignedTo;
    private String assignedManager;
}