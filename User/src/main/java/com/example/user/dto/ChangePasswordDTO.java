package com.example.user.dto;

import java.util.UUID;

public class ChangePasswordDTO {
    private UUID userId;
    private String newPassword;

    // getters and setters
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}