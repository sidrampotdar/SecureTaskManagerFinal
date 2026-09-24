package com.example.securetaskmanagerfinal.controllers;

import com.example.securetaskmanagerfinal.dto.LoginReq;
import com.example.securetaskmanagerfinal.dto.RegisterRequest;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.entity.dto.AuthResponse;
import com.example.securetaskmanagerfinal.entity.dto.RefreshReq;
import com.example.securetaskmanagerfinal.service.AuthService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User registerUser(@RequestBody RegisterRequest request) {
        return authService.registerUser(request);
    }
//    @PostMapping("/login")
//    public String login(
//            @RequestBody LoginReq request
//    ) {
//
//        Authentication authentication =
//                authService.login(request);
//
//        return "Login successful for: "
//                + authentication.getName();
//    }
@PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginReq request
    ){
        return authService.login(request);
    }
    @PostMapping("/refresh")
    public AuthResponse refresh(
            @RequestBody RefreshReq request
    ) {
        return authService.refresh(
                request.refreshToken()
        );
    }
    public ResponseEntity<Void> logout(@RequestBody
                                       RefreshReq   refreshReq
                                       ) {
        authService.logout(refreshReq.refreshToken());
        return  ResponseEntity.noContent().build();
    }
}
