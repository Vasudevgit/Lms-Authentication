package com.example.authapplication.repository;

import com.example.authapplication.entity.Role;
import com.example.authapplication.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 🔥 REQUIRED FOR LOGIN & JWT
    Optional<User> findByEmail(String email);

    // 🔥 REQUIRED FOR DROPDOWN
    List<User> findByRole(Role role);
}
