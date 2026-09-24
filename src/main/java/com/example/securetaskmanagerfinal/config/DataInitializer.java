package com.example.securetaskmanagerfinal.config;

import com.example.securetaskmanagerfinal.entity.Permission;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.entity.User;
import com.example.securetaskmanagerfinal.repository.PermissionRepository;
import com.example.securetaskmanagerfinal.repository.RoleRepository;
import com.example.securetaskmanagerfinal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

    @Value("${admin.email:admin@example.com}")
    private String adminEmail;

    @Value("${admin.password:ChangeThisImmediately123!}")
    private String adminPassword;

    @Bean
    CommandLineRunner init(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // 1. Task Permissions
            Permission taskRead = createPermission(permissionRepository, "TASK_READ");
            Permission taskCreate = createPermission(permissionRepository, "TASK_CREATE");
            Permission taskUpdate = createPermission(permissionRepository, "TASK_UPDATE");
            Permission taskDelete = createPermission(permissionRepository, "TASK_DELETE");

            // 2. User Management Permissions
            Permission userRead = createPermission(permissionRepository, "USER_READ");
            Permission userCreate = createPermission(permissionRepository, "USER_CREATE");
            Permission userUpdate = createPermission(permissionRepository, "USER_UPDATE");
            Permission userDelete = createPermission(permissionRepository, "USER_DELETE");

            // 3. Roles
            Role userRole = createRole(roleRepository, "USER");
            Role adminRole = createRole(roleRepository, "ADMIN");

            // 4. Assign Permissions to USER Role
            userRole.getPermissions().addAll(List.of(
                    taskRead, taskCreate, taskUpdate, taskDelete
            ));

            // 5. Assign Permissions to ADMIN Role
            adminRole.getPermissions().addAll(List.of(
                    taskRead, taskCreate, taskUpdate, taskDelete,
                    userRead, userCreate, userUpdate, userDelete
            ));

            roleRepository.save(userRole);
            roleRepository.save(adminRole);

            // 6. Bootstrap Initial Admin
            if (userRepository.findByEmail(adminEmail).isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail(adminEmail);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.getRoles().add(adminRole);

                userRepository.save(admin);
                System.out.println("Initial ADMIN created: " + adminEmail);
            }
        };
    }

    private Permission createPermission(
            PermissionRepository repository,
            String name
    ) {
        return repository
                .findByName(name)
                .orElseGet(() ->
                        repository.save(
                                new Permission(name)
                        )
                );
    }

    private Role createRole(
            RoleRepository repository,
            String name
    ) {
        return repository
                .findByName(name)
                .orElseGet(() ->
                        repository.save(
                                new Role(name)
                        )
                );
    }
}