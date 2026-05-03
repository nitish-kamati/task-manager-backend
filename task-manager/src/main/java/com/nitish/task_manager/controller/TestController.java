package com.nitish.task_manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<Map<String, Object>> publicTest() {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Public API working",
                        "status", 200
                )
        );
    }
    @GetMapping("/test-secure")
    public ResponseEntity<Map<String, Object>> secureTest(Authentication authentication) {
        return ResponseEntity.ok(
                Map.of(
                        "message", "Protected API working",
                        "user", authentication.getName(),
                        "status", 200
                )
        );
    }
}