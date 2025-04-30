package com.example.user.command;

import com.example.user.service.UserService;

public class ChangePasswordCommand implements Command {

    private final UserService userService;
    private final Long userId;
    private final String newPassword;

    public ChangePasswordCommand(UserService userService, Long userId, String newPassword) {
        this.userService = userService;
        this.userId = userId;
        this.newPassword = newPassword;
    }

    @Override
    public void execute() {
        userService.changePassword(userId, newPassword);
    }
}
