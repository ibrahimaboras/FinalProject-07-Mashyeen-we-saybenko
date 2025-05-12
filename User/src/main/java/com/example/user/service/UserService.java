package com.example.user.service;

import com.example.user.config.SingletonSessionManager;
import com.example.user.dto.PastFlightDTO;
import com.example.user.model.User;
import com.example.user.model.UserProfile;
import com.example.user.repository.UserProfileRepository;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

// use command

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final RestTemplate restTemplate;

    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository,
                       RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplate;
    }

    // Register User
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists!");
        }
        return userRepository.save(user);
    }

    // Login
    public User login(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optionalUser.get();
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Incorrect password");
        }

        SingletonSessionManager.getInstance().startSession(user.getUserId());
        return user;
    }

    // Logout
    public String logout(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        SingletonSessionManager.getInstance().endSession(userId);
        return "User logged out successfully.";
    }

    // Change Password
    public void changePassword(Long userId, String newPassword) {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(newPassword);
        userRepository.save(user);
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    public void deleteUser(Long userId) {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
        SingletonSessionManager.getInstance().endSession(userId); // Optional: clean up session
    }

    // Update Profile
    public UserProfile updateUserProfile(Long userId, UserProfile updatedProfile) {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserProfile profile = user.getProfile();

        if (profile == null) {
            updatedProfile.setUser(user);
            return userProfileRepository.save(updatedProfile);
        } else {
            profile.setNationality(updatedProfile.getNationality());
            profile.setPassportNumber(updatedProfile.getPassportNumber());
            profile.setGender(updatedProfile.getGender());
            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
            return userProfileRepository.save(profile);
        }
    }

    // View Past Flights
    public List<PastFlightDTO> viewPastFlights(Long userId) {
        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
            throw new RuntimeException("User is not logged in.");
        }

        String url = bookingServiceUrl + "/bookings/past-flights/" + userId;

        try {
            PastFlightDTO[] response = restTemplate.getForObject(url, PastFlightDTO[].class);
            return Arrays.asList(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve past flights: " + e.getMessage());
        }
    }
}