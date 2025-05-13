package com.example.user.command;

import com.example.user.dto.LoginDTO;
import com.example.user.model.User;
import com.example.user.service.UserService;

public class LoginCommand implements Command {

    private final UserService userService;
    private final LoginDTO loginDTO;

    public LoginCommand(UserService userService, LoginDTO loginDTO) {
        this.userService = userService;
        this.loginDTO = loginDTO;
    }

    @Override
    public void execute() {
        userService.login(loginDTO.getEmail(), loginDTO.getPassword());
    }
}
