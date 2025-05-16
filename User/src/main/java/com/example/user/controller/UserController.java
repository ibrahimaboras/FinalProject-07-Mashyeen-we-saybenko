package com.example.user.controller;

import com.example.user.command.*;
import com.example.user.dto.ChangePasswordDTO;
import com.example.user.dto.LoginDTO;
import com.example.user.dto.RegisterDTO;
import com.example.user.dto.PastFlightDTO;
import com.example.user.model.User;
import com.example.user.model.UserProfile;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register User
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterDTO dto) {
        User user = new User(dto.getFullName(), dto.getEmail(), dto.getPassword(), dto.getPhone());
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto.getEmail(), dto.getPassword()));
    }

    // Logout
    @PostMapping("/logout/{userId}")
    public ResponseEntity<?> logout(@PathVariable Long userId) {
           return ResponseEntity.ok(userService.logout(userId));
    }

    // Change Password
    @PutMapping("/change-password")
    public ResponseEntity<?>changePassword(@RequestBody ChangePasswordDTO dto) {
        return userService.changePassword(dto.getUserId(), dto.getNewPassword());
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/{domain}")
    public ResponseEntity<ArrayList<User>> findUserByEmail(@PathVariable String email){
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @GetMapping("/{fullName}")
    public ResponseEntity<User> findUserByFullName(@PathVariable String fullName){
        return ResponseEntity.ok(userService.findUserByFullName(fullName));
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }

    // Update or add user profile
    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateProfile(@PathVariable Long userId, @RequestBody UserProfile profile) {
        return userService.updateUserProfileWithMessage(userId, profile);
    }
    // Get user by full name
    @GetMapping("/by-name")
    public ResponseEntity<User> getUserByFullName(@RequestParam String fullName) {
        return ResponseEntity.ok(userService.getUserByFullName(fullName));
    }

    // Get user by email
    @GetMapping("/by-email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}