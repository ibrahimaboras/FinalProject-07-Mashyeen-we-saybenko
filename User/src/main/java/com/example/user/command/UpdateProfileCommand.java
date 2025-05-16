package com.example.user.command;

import com.example.user.model.UserProfile;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class UpdateProfileCommand implements Command {

    private final UserService userService;
    private final UUID userId;
    private final UserProfile updatedProfile;

    public UpdateProfileCommand(UserService userService, UUID userId, UserProfile updatedProfile) {
        this.userService = userService;
        this.userId = userId;
        this.updatedProfile = updatedProfile;
    }

    @Override
    public ResponseEntity<?> execute() {
        return ResponseEntity.ok(userService.updateUserProfile(userId, updatedProfile));
    }
}
