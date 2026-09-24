package com.example.securetaskmanagerfinal.controllers;

import com.example.securetaskmanagerfinal.dto.CreateUserRequest;
import com.example.securetaskmanagerfinal.dto.UserResponse;
import com.example.securetaskmanagerfinal.service.AdminUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @PostMapping
    public UserResponse createUser(
            @RequestBody CreateUserRequest request
    ) {
        return adminUserService.createUser(request);
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return adminUserService.getAllUsers();
    }
}
