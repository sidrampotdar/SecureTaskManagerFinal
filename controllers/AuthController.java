package com.example.securetaskmanagerfinal.controllers;

import com.example.securetaskmanagerfinal.dto.RegisterRequest;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }

}
