package com.example.authapplication.service;

import com.example.authapplication.dto.response.PasswordResetAdminResponse;
import com.example.authapplication.entity.*;
import com.example.authapplication.repository.PasswordResetRequestRepository;
import com.example.authapplication.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PasswordResetService {

    private final PasswordResetRequestRepository resetRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            PasswordResetRequestRepository resetRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.resetRepository = resetRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= USER REQUEST =================

    public void requestReset(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean alreadyPending =
                resetRepository.findByUserIdAndStatus(
                        user.getId(),
                        ResetStatus.PENDING
                ).isPresent();

        if (alreadyPending) {
            throw new RuntimeException("Reset request already pending");
        }

        PasswordResetRequest request = new PasswordResetRequest();
        request.setUserId(user.getId());
        request.setStatus(ResetStatus.PENDING);
        request.setRequestedAt(LocalDateTime.now());

        resetRepository.save(request);
    }

    // ================= ADMIN APPROVE =================

    public void approveResetByEmail(Long requestId, String adminEmail) {

        PasswordResetRequest request =
                resetRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        if (request.getStatus() != ResetStatus.PENDING) {
            throw new RuntimeException("Already handled");
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPasswordHash(passwordEncoder.encode("1234"));
        user.setForcePwdChange(true);
        userRepository.save(user);

        request.setStatus(ResetStatus.COMPLETED);
        request.setHandledBy(admin.getId());
        request.setHandledAt(LocalDateTime.now());

        resetRepository.save(request);
    }

    // ================= ADMIN REJECT =================

    public void rejectResetByEmail(Long requestId, String adminEmail) {

        PasswordResetRequest request =
                resetRepository.findById(requestId)
                        .orElseThrow(() ->
                                new RuntimeException("Request not found"));

        if (request.getStatus() != ResetStatus.PENDING) {
            throw new RuntimeException("Already handled");
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        request.setStatus(ResetStatus.REJECTED);
        request.setHandledBy(admin.getId());
        request.setHandledAt(LocalDateTime.now());

        resetRepository.save(request);
    }

    // ================= ADMIN LIST =================

    public List<PasswordResetAdminResponse> getByStatus(ResetStatus status) {

        List<PasswordResetRequest> requests =
                resetRepository.findByStatus(status);

        return requests.stream().map(req -> {

            User user = userRepository.findById(req.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return new PasswordResetAdminResponse(
                    req.getId(),
                    user.getName(),
                    user.getEmail(),
                    req.getRequestedAt(),
                    req.getStatus().name()
            );

        }).toList();
    }
}
