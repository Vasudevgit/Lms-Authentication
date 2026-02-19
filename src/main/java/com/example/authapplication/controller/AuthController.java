package com.example.authapplication.controller;

import com.example.authapplication.dto.request.ChangePasswordRequest;
import com.example.authapplication.dto.request.LoginRequest;
import com.example.authapplication.dto.response.LoginResponse;
import com.example.authapplication.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request);
    }
}
