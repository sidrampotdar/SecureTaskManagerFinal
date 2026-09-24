package com.example.securetaskmanagerfinal.dto;

import com.example.securetaskmanagerfinal.entity.TaskStatus;

import java.time.LocalDateTime;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Long ownerId,
        String ownerEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}