package com.example.securetaskmanagerfinal.controllers;

import com.example.securetaskmanagerfinal.dto.TaskResponse;
import com.example.securetaskmanagerfinal.dto.UpdateTaskRequest;
import com.example.securetaskmanagerfinal.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/tasks")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTaskController {

    private final TaskService taskService;

    public AdminTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTask(
            @PathVariable Long id
    ) {
        return taskService.getTaskAsAdmin(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @RequestBody UpdateTaskRequest request
    ) {
        return taskService.updateTaskAsAdmin(
                id,
                request
        );
    }

    @DeleteMapping("/{id}")
    public void deleteTask(
            @PathVariable Long id
    ) {
        taskService.deleteTaskAsAdmin(id);
    }
}
