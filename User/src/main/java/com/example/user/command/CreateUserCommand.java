package com.example.user.command;

import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<?> execute() {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Email already exists!");
        }
        User savedUser= userRepository.save(user);
        return ResponseEntity.ok(savedUser);
//        return ResponseEntity.ok( userService.registerUser(user));
    }

}
