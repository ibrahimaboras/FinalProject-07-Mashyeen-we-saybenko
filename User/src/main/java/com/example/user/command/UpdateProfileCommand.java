package com.example.user.command;

import com.example.user.model.UserProfile;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

public class UpdateProfileCommand implements Command {

    private final UserService userService;
    private final Long userId;
    private final UserProfile updatedProfile;

    public UpdateProfileCommand(UserService userService, Long userId, UserProfile updatedProfile) {
        this.userService = userService;
        this.userId = userId;
        this.updatedProfile = updatedProfile;
    }

    @Override
    public ResponseEntity<?> execute() {
        return ResponseEntity.ok(userService.updateUserProfile(userId, updatedProfile));
    }
}
