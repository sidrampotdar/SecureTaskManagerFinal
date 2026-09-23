package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.dto.LoginReq;
import com.example.securetaskmanagerfinal.dto.RegisterRequest;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.RoleRepository;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

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
        Role userRole =
                roleRepository
                        .findByName("USER")
                        .orElseThrow();

        user.getRoles().add(userRole);
        return userRepository.save(user);
    }

//    public Authentication login(LoginReq request) {
//
//        Authentication authentication =
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                );
//
//        return authenticationManager.authenticate(authentication);
//    }




    public String login(LoginReq loginReq){
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginReq.getEmail(),
                                loginReq.getPassword()
                        )
                );
        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        return jwtService.generateToken(userDetails);
    }

}
