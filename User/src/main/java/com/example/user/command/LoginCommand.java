package com.example.user.command;

import com.example.user.config.SingletonSessionManager;
import com.example.user.dto.LoginDTO;
import com.example.user.model.User;
import com.example.user.repository.UserRepository;
import com.example.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LoginCommand implements Command {

    private final UserService userService;
    private final LoginDTO loginDTO;
    private final UserRepository userRepository;

    public LoginCommand(UserService userService, LoginDTO loginDTO, UserRepository userRepository) {
        this.userService = userService;
        this.loginDTO = loginDTO;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> execute() {
       // return ResponseEntity.ok(userService.login(loginDTO.getEmail(), loginDTO.getPassword()));
        Optional<User> optionalUser = userRepository.findByEmail(loginDTO.getEmail());
        if (optionalUser.isEmpty()) {
            return  buildErrorResponse(HttpStatus.UNAUTHORIZED, "User not found");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(loginDTO.getPassword())) {
            return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Incorrect password");
        }

        SingletonSessionManager.getInstance().startSession(user.getUserId());
        return ResponseEntity.ok(user);
    }
    // Helper method to build structured error response
    private ResponseEntity<Map<String, String>> buildErrorResponse(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return ResponseEntity.status(status).body(response);
    }
}
