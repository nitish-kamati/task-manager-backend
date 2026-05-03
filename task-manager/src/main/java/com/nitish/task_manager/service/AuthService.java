package com.nitish.task_manager.service;

import com.nitish.task_manager.config.JwtUtil;
import com.nitish.task_manager.dto.LoginRequestDTO;
import com.nitish.task_manager.dto.LoginResponseDTO;
import com.nitish.task_manager.dto.RegisterRequestDTO;
import com.nitish.task_manager.model.User;
import com.nitish.task_manager.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public User register(RegisterRequestDTO dto) {

        if (dto.getEmail() == null || dto.getPassword() == null) {
            throw new RuntimeException("Email or Password missing");
        }

        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.setRole(dto.getRole() != null ? dto.getRole() : "EMPLOYEE");

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        if (dto.getEmail() == null || dto.getPassword() == null) {
            throw new RuntimeException("Email or password missing");
        }

        String email = dto.getEmail().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponseDTO(
                token,
                user.getRole()
        );
    }
}