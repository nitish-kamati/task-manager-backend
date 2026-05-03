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

    // 🔥 REGISTER LOGIC
    public User register(RegisterRequestDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        return userRepository.save(user);
    }

    // 🔥 LOGIN LOGIC
    public LoginResponseDTO login(LoginRequestDTO dto) {

        System.out.println("INPUT EMAIL: " + dto.getEmail());
        User existingUser = userRepository.findByEmail(dto.getEmail().trim().toLowerCase());


        System.out.println("DB USER: " + existingUser);
        if (existingUser == null) {
            throw new RuntimeException("Invalid credentials");
        }

        boolean isMatch = passwordEncoder.matches(
                dto.getPassword(),
                existingUser.getPassword()
        );

        if (!isMatch) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(
                existingUser.getEmail(),
                existingUser.getRole()
        );

        return new LoginResponseDTO(
                token,
                existingUser.getRole()
        );
    }
}