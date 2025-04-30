package com.example.user.command;

import com.example.user.model.User;
import com.example.user.service.UserService;

public class CreateUserCommand implements Command {

    private final UserService userService;
    private final User user;

    public CreateUserCommand(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
    }

    @Override
    public void execute() {
        userService.registerUser(user);
    }
}
