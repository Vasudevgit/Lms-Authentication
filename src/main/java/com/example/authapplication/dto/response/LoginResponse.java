package com.example.authapplication.dto.response;

public class LoginResponse {

    private Long id;
    private String token;
    private String role;
    private boolean forcePasswordChange;

    public LoginResponse(Long id, String token, String role, boolean forcePasswordChange) {
        this.id = id;
        this.token = token;
        this.role = role;
        this.forcePasswordChange = forcePasswordChange;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setForcePasswordChange(boolean forcePasswordChange) {
        this.forcePasswordChange = forcePasswordChange;
    }

    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public boolean isForcePasswordChange() {
        return forcePasswordChange;
    }


}
