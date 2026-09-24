package com.example.securetaskmanagerfinal.entity.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
