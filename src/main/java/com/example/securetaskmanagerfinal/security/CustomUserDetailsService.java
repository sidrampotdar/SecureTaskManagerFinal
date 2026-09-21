package com.example.securetaskmanagerfinal.security;

import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService  implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        Spring Security gave me a username. Where is that user?
        User user = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(
                        "User not found with email: " + username
                ));
        return new CustomUserDetails(user);
    }
}
