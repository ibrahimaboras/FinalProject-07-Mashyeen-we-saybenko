package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

public class LogoutCommand implements Command {

    private final UserService userService;
    private final Long userId;
    private final UserRepository userRepository;

    public LogoutCommand(UserService userService, Long userId, UserRepository userRepository) {
        this.userService = userService;
        this.userId = userId;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?>  execute() {

        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(404).body("User not found");
        }

        SingletonSessionManager.getInstance().endSession(userId);
        return ResponseEntity.ok("User logged out successfully");
    }
}
