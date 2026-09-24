package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.dto.CreateUserRequest;
import com.example.securetaskmanagerfinal.dto.UserResponse;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.RoleRepository;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName(request.role())
                .orElseThrow(() -> new RuntimeException("Role not found: " + request.role()));

        User user = new User();
        user.setUsername(request.username() != null && !request.username().isBlank()
                ? request.username()
                : request.email().split("@")[0]);
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);
        return toResponse(savedUser);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
        );
    }
}
