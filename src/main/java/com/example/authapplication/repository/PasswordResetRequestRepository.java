package com.example.authapplication.repository;

import com.example.authapplication.entity.PasswordResetRequest;
import com.example.authapplication.entity.ResetStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PasswordResetRequestRepository
        extends JpaRepository<PasswordResetRequest, Long> {

    Optional<PasswordResetRequest> findByUserIdAndStatus(Long userId, ResetStatus status);

    List<PasswordResetRequest> findByStatus(ResetStatus status);

}
