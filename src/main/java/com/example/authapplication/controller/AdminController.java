package com.example.authapplication.controller;

import com.example.authapplication.dto.request.CreateUserRequest;
import com.example.authapplication.dto.response.UserDropdownResponse;
import com.example.authapplication.entity.Role;
import com.example.authapplication.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public void createUser(@RequestBody CreateUserRequest request) {
        adminService.createUser(request);
    }

    @PostMapping("/reset-password/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public void resetPassword(@PathVariable Long userId) {
        adminService.resetPassword(userId);
    }

    @GetMapping("/eligible-managers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDropdownResponse> getEligibleManagers(
            @RequestParam Role role) {

        return adminService.getEligibleManagers(role);
    }
}
