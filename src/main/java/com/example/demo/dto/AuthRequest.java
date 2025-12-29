package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class AuthRequest {

    @Schema(example = "user@example.com")
    private String email;

    @Schema(example = "password123")
    private String password;

    @Schema(example = "USER")
    private String role;

    public AuthRequest() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}
