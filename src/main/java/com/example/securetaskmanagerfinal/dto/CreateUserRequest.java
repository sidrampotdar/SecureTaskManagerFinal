package com.example.securetaskmanagerfinal.dto;

public record CreateUserRequest(
        String username,
        String email,
        String password,
        String role
) {
}
