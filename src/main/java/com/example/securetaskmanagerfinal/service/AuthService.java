package com.example.securetaskmanagerfinal.service;

import com.example.securetaskmanagerfinal.dto.LoginReq;
import com.example.securetaskmanagerfinal.dto.RegisterRequest;
import com.example.securetaskmanagerfinal.entity.RefreshToken;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.entity.dto.AuthResponse;
import com.example.securetaskmanagerfinal.repository.RoleRepository;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import com.example.securetaskmanagerfinal.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final RefreshTokenService refreshTokenService;

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


    public AuthResponse login(LoginReq request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        CustomUserDetails userDetails =
                (CustomUserDetails)
                        authentication.getPrincipal();

        String accessToken =
                jwtService.generateToken(userDetails);

        String refreshToken =
                refreshTokenService.createRefreshToken(
                        userDetails.getUser()
                );

        return new AuthResponse(
                accessToken,
                refreshToken
        );
    }
    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {

        RefreshToken oldToken =
                refreshTokenService.validate(
                        rawRefreshToken
                );

        User user = oldToken.getUser();

        CustomUserDetails userDetails =
                new CustomUserDetails(user);

        String newAccessToken =
                jwtService.generateToken(userDetails);

        refreshTokenService.revoke(oldToken);

        String newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                newAccessToken,
                newRefreshToken
        );
    }

    public void logout(String rawRefreshToken){
        RefreshToken token = refreshTokenService.validate(rawRefreshToken);
        refreshTokenService.revoke(token);
    }
}
