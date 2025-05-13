package com.example.user.command;

import com.example.user.service.UserService;

public class LogoutCommand implements Command {

    private final UserService userService;
    private final Long userId;

    public LogoutCommand(UserService userService, Long userId) {
        this.userService = userService;
        this.userId = userId;
    }

    @Override
    public void execute() {
        userService.logout(userId);
    }
}
