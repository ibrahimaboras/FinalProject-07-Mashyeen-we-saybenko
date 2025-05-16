package com.example.user.command;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;

public class CreateUserCommand implements Command {

    private final UserService userService;
    private final User user;
    private final UserRepository userRepository;

    public CreateUserCommand(UserService userService, User user, UserRepository userRepository) {
        this.userService = userService;
        this.user = user;
        this.userRepository = userRepository;
    }

    @Override
    public User execute() {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        return userRepository.save(user);
//        return ResponseEntity.ok( userService.registerUser(user));
    }
}
