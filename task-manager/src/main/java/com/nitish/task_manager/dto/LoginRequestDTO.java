package com.nitish.task_manager.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}