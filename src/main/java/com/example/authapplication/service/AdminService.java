package com.example.authapplication.service;

import com.example.authapplication.dto.request.CreateUserRequest;
import com.example.authapplication.dto.response.UserDropdownResponse;
import com.example.authapplication.entity.Role;
import com.example.authapplication.entity.Status;
import com.example.authapplication.entity.User;
import com.example.authapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= CREATE USER =================

    public void createUser(CreateUserRequest request) {

        // 🔴 EMAIL DUPLICATE CHECK (MANDATORY)
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPasswordHash(passwordEncoder.encode("1234"));
        user.setForcePwdChange(true);
        user.setStatus(Status.ACTIVE);

        Role role = request.getRole();

        // 🔐 MANAGER ASSIGNMENT RULES
        if (role == Role.EMPLOYEE) {

            if (request.getManagerId() == null) {
                throw new RuntimeException("Manager is required for employee");
            }

            User manager = getUserOrThrow(request.getManagerId());

            if (manager.getRole() != Role.MANAGER) {
                throw new RuntimeException("Assigned manager must be MANAGER");
            }

            user.setManagerId(manager.getId());
        }

        else if (role == Role.MANAGER || role == Role.ADMIN) {

            if (request.getManagerId() == null) {
                throw new RuntimeException("HR must be assigned");
            }

            User hr = getUserOrThrow(request.getManagerId());

            if (hr.getRole() != Role.HR) {
                throw new RuntimeException("Assigned manager must be HR");
            }

            user.setManagerId(hr.getId());
        }

        else if (role == Role.HR) {
            user.setManagerId(null);
        }

        userRepository.save(user);
    }

    // ================= DROPDOWN =================

    public List<UserDropdownResponse> getEligibleManagers(Role role) {

        List<User> users;

        if (role == Role.EMPLOYEE) {
            users = userRepository.findByRole(Role.MANAGER);
        } else if (role == Role.MANAGER || role == Role.ADMIN) {
            users = userRepository.findByRole(Role.HR);
        } else {
            return new java.util.ArrayList<>();
        }

        return users.stream()
                .map(u -> new UserDropdownResponse(u.getId(), u.getName()))
                .toList();
    }

    // ================= HELPER =================

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    public void resetPassword(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode("1234"));
        user.setForcePwdChange(true);

        userRepository.save(user);
    }

}
