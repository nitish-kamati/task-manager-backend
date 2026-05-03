package com.nitish.task_manager.controller;

import com.nitish.task_manager.dto.LoginRequestDTO;
import com.nitish.task_manager.dto.LoginResponseDTO;
import com.nitish.task_manager.dto.RegisterRequestDTO;
import com.nitish.task_manager.model.User;
import com.nitish.task_manager.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

//    @GetMapping("/test-secure")
//    public ResponseEntity<String> secure() {
//        return ResponseEntity.ok("This is protected 🔐");
//    }
}