package com.miniProject.EduBlog.dto;

public class AuthResponse {
    private String token;
    private String message;
    private boolean success;

    public AuthResponse(String token) {
        this.token = token;
        this.success = token != null && !token.startsWith("Error") && !token.startsWith("Invalid");
        this.message = this.success ? "Authentication successful" : token;
    }

    // Getters
    public String getToken() {
        return success ? token : null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }
}