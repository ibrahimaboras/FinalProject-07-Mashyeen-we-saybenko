package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public class DeleteUserCommand implements Command {

    private final UserService userService;
    private final UUID userId;
    private final UserRepository userRepository;

    public DeleteUserCommand(UserService userService, UUID userId, UserRepository userRepository) {
        this.userService = userService;
        this.userId = userId;
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

        userRepository.deleteById(userId);
        SingletonSessionManager.getInstance().endSession(userId); // Optional: clean up session
        return ResponseEntity.ok("User deleted successfully");
        }
}
