package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.dto.CreateTaskRequest;
import com.example.securetaskmanagerfinal.dto.TaskResponse;
import com.example.securetaskmanagerfinal.dto.UpdateTaskRequest;
import com.example.securetaskmanagerfinal.entity.Task;
import com.example.securetaskmanagerfinal.entity.TaskStatus;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(
            CreateTaskRequest request,
            User currentUser
    ) {

        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());

        task.setStatus(
                request.status() != null
                        ? request.status()
                        : TaskStatus.PENDING
        );

        task.setOwner(currentUser);

        Task saved = taskRepository.save(task);

        return toResponse(saved);
    }

    public List<TaskResponse> getMyTasks(
            User currentUser
    ) {

        return taskRepository
                .findByOwnerId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse getMyTask(
            Long taskId,
            User currentUser
    ) {

        Task task =
                taskRepository
                        .findByIdAndOwnerId(
                                taskId,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        return toResponse(task);
    }

    public TaskResponse updateMyTask(
            Long taskId,
            UpdateTaskRequest request,
            User currentUser
    ) {

        Task task =
                taskRepository
                        .findByIdAndOwnerId(
                                taskId,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        task.setTitle(request.title());
        task.setDescription(request.description());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        return toResponse(
                taskRepository.save(task)
        );
    }

    public void deleteMyTask(
            Long taskId,
            User currentUser
    ) {

        Task task =
                taskRepository
                        .findByIdAndOwnerId(
                                taskId,
                                currentUser.getId()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        taskRepository.delete(task);
    }

    // ADMIN OPERATIONS

    public List<TaskResponse> getAllTasks() {

        return taskRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TaskResponse getTaskAsAdmin(Long taskId) {

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        return toResponse(task);
    }

    public TaskResponse updateTaskAsAdmin(
            Long taskId,
            UpdateTaskRequest request
    ) {

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        task.setTitle(request.title());
        task.setDescription(request.description());

        if (request.status() != null) {
            task.setStatus(request.status());
        }

        return toResponse(
                taskRepository.save(task)
        );
    }

    public void deleteTaskAsAdmin(Long taskId) {

        Task task =
                taskRepository.findById(taskId)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Task not found"
                                )
                        );

        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {

        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getOwner().getId(),
                task.getOwner().getEmail(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
