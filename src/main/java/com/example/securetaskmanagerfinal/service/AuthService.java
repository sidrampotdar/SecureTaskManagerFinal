package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.dto.LoginReq;
import com.example.securetaskmanagerfinal.dto.RegisterRequest;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User registerUser(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email already exists");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = new User(
                request.getUsername(),
                request.getEmail(),
                hashedPassword
        );
        return userRepository.save(user);
    }

    public Authentication login(LoginReq request) {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                );

        return authenticationManager.authenticate(authentication);
    }
}
