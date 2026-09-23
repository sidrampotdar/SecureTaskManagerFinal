package com.example.securetaskmanagerfinal.config;


import com.example.securetaskmanagerfinal.entity.Permission;
import com.example.securetaskmanagerfinal.entity.Role;
import com.example.securetaskmanagerfinal.repository.PermissionRepository;
import com.example.securetaskmanagerfinal.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            PermissionRepository permissionRepository,
            RoleRepository roleRepository
    ) {
        return args -> {

            Permission productRead =
                    createPermission(
                            permissionRepository,
                            "PRODUCT_READ"
                    );

            Permission productWrite =
                    createPermission(
                            permissionRepository,
                            "PRODUCT_WRITE"
                    );

            Permission userDelete =
                    createPermission(
                            permissionRepository,
                            "USER_DELETE"
                    );

            Role userRole =
                    createRole(
                            roleRepository,
                            "USER"
                    );

            Role adminRole =
                    createRole(
                            roleRepository,
                            "ADMIN"
                    );

            userRole.getPermissions()
                    .add(productRead);

            adminRole.getPermissions()
                    .add(productRead);

            adminRole.getPermissions()
                    .add(productWrite);

            adminRole.getPermissions()
                    .add(userDelete);

            roleRepository.save(userRole);
            roleRepository.save(adminRole);
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