package com.example.authapplication.dto.response;

import java.time.LocalDateTime;

public class PasswordResetAdminResponse {

    private Long requestId;
    private String name;
    private String email;
    private LocalDateTime requestedAt;
    private String status;

    public PasswordResetAdminResponse(Long requestId,
                                      String name,
                                      String email,
                                      LocalDateTime requestedAt,
                                      String status) {
        this.requestId = requestId;
        this.name = name;
        this.email = email;
        this.requestedAt = requestedAt;
        this.status = status;
    }

    public Long getRequestId() {
        return requestId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public String getStatus() {
        return status;
    }
}
