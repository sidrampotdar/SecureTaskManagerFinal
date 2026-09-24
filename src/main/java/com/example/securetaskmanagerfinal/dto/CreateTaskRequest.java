package com.example.securetaskmanagerfinal.dto;

import com.example.securetaskmanagerfinal.entity.TaskStatus;

public record CreateTaskRequest(
        String title,
        String description,
        TaskStatus status
) {
}