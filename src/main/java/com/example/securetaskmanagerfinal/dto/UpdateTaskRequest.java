package com.example.securetaskmanagerfinal.dto;

import com.example.securetaskmanagerfinal.entity.TaskStatus;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status
) {
}
