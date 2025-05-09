package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;

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
    public Object execute() {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);
        return null;
    }
}