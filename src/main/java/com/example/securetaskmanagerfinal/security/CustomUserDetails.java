package com.example.securetaskmanagerfinal.security;

import com.example.securetaskmanagerfinal.entity.Permission;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.entity.User;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
//Spring Security, take my application's user and
// expose the information you need through the UserDetails contract.

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<GrantedAuthority> authorities =
                new HashSet<>();

        for (Role role : user.getRoles()) {

            // ROLE_ADMIN
            authorities.add(
                    new SimpleGrantedAuthority(
                            "ROLE_" + role.getName()
                    )
            );

            // PRODUCT_READ
            // PRODUCT_WRITE
            // etc.
            for (Permission permission :
                    role.getPermissions()) {

                authorities.add(
                        new SimpleGrantedAuthority(
                                permission.getName()
                        )
                );
            }
        }

        return authorities;
    }
    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
      return   user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public User getUser() {
        return user;
    }
}
