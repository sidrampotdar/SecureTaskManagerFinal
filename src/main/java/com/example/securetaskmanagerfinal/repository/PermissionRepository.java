package com.example.securetaskmanagerfinal.repository;

import com.example.securetaskmanagerfinal.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository  extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
}
