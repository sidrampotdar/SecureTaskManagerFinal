package com.example.securetaskmanagerfinal.controllers;

import com.example.securetaskmanagerfinal.dto.CreateTaskRequest;
import com.example.securetaskmanagerfinal.dto.TaskResponse;
import com.example.securetaskmanagerfinal.dto.UpdateTaskRequest;
import com.example.securetaskmanagerfinal.security.CustomUserDetails;
import com.example.securetaskmanagerfinal.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('TASK_CREATE')")
    @PostMapping
    public TaskResponse createTask(
            @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return taskService.createTask(
                request,
                userDetails.getUser()
        );
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping
    public List<TaskResponse> getMyTasks(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return taskService.getMyTasks(
                userDetails.getUser()
        );
    }

    @PreAuthorize("hasAuthority('TASK_READ')")
    @GetMapping("/{id}")
    public TaskResponse getMyTask(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return taskService.getMyTask(
                id,
                userDetails.getUser()
        );
    }

    @PreAuthorize("hasAuthority('TASK_UPDATE')")
    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return taskService.updateMyTask(
                id,
                request,
                userDetails.getUser()
        );
    }

    @PreAuthorize("hasAuthority('TASK_DELETE')")
    @DeleteMapping("/{id}")
    public void deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        taskService.deleteMyTask(
                id,
                userDetails.getUser()
        );
    }
}
