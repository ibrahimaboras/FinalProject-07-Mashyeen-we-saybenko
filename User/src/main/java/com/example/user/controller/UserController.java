package com.example.user.controller;

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
    public ResponseEntity<User> registerUser(@RequestBody RegisterDTO dto) {
        User user = new User(dto.getFullName(), dto.getEmail(), dto.getPassword(), dto.getPhone());
        return ResponseEntity.ok(userService.registerUser(user));
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(userService.login(dto.getEmail(), dto.getPassword()));
    }

    // Logout
    @PostMapping("/logout/{userId}")
    public ResponseEntity<String> logout(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.logout(userId));
    }

    // Change Password
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto.getUserId(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    // View Past Flights
    @GetMapping("/{userId}/past-flights")
    public ResponseEntity<List<PastFlightDTO>> viewPastFlights(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.viewPastFlights(userId));
    }

    // Get user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Delete user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Update or add user profile
    @PutMapping("/{userId}/profile")
    public ResponseEntity<UserProfile> updateProfile(@PathVariable Long userId, @RequestBody UserProfile profile) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, profile));
    }
}
