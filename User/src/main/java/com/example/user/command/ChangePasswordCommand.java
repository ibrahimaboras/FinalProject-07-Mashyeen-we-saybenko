package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;


public class ChangePasswordCommand implements Command {

    private final UserService userService;
    private final Long userId;
    private final String newPassword;
    private final UserRepository userRepository;

    public ChangePasswordCommand(UserService userService, Long userId, String newPassword, UserRepository userRepository) {
        this.userService = userService;
        this.userId = userId;
        this.newPassword = newPassword;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> execute() {
        // First: check if user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Then: check if user is logged in
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            return ResponseEntity.status(401).body("User is not logged in");
        }

        // Update password
        user.setPassword(newPassword);
        userRepository.save(user);
        return ResponseEntity.ok("Password updated successfully");
    }
}