package com.example.securetaskmanagerfinal.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 1. Any authenticated user can access this endpoint
    @GetMapping("/test")
    public String test() {
        return "Hello, Your Spring App is running successfully!";
    }

    // 2. Any authenticated user can access this endpoint
    @GetMapping("/user")
    public String user() {
        return "Any authenticated user";
    }

    // 3. Role-Based Access: Only users with the 'ADMIN' role (ROLE_ADMIN) can access
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminOnly() {
        return "Admin area";
    }

    // 4. Permission-Based Access: Only users with 'PRODUCT_WRITE' permission can access
    @PreAuthorize("hasAuthority('PRODUCT_WRITE')")
    @PostMapping("/product")
    public String product() {
        return "PRODUCT_WRITE permission required";
    }
}

