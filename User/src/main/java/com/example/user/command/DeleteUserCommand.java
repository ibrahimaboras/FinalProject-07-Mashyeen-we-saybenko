package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

public class DeleteUserCommand implements Command {

    private final UserService userService;
    private final Long userId;
    private final UserRepository userRepository;

    public DeleteUserCommand(UserService userService, Long userId, UserRepository userRepository) {
        this.userService = userService;
        this.userId = userId;
        this.userRepository = userRepository;
    }

    @Override
    public Object execute() {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
        SingletonSessionManager.getInstance().endSession(userId); // Optional: clean up session
        return null;
        }
}
