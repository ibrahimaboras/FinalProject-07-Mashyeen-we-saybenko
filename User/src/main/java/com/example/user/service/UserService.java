package com.example.user.service;

import com.example.user.command.*;
import com.example.user.config.SingletonSessionManager;
import com.example.user.dto.LoginDTO;
import com.example.user.dto.PastFlightDTO;
import com.example.user.model.User;
import com.example.user.model.UserProfile;
import com.example.user.repository.UserProfileRepository;
import com.example.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.*;

// use command

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;


    private CreateUserCommand createUserCommand;
    private ChangePasswordCommand changePasswordCommand;
    private DeleteUserCommand deleteUserCommand;
    private LoginCommand loginCommand;
    private LogoutCommand logoutCommand;
    private UpdateProfileCommand updateProfileCommand;
    private RestTemplate restTemplate;


    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository, RestTemplate restTemplate
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplate;
    }

    // Register User
    public  ResponseEntity<?> registerUser(User user) {
        createUserCommand = new CreateUserCommand(this, user, this.userRepository);
        return createUserCommand.execute();
    }

    // Login
    //Consider for better security not giving them a direct answer of whether it is the email or password??
    public ResponseEntity<?> login(String email, String password) {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setEmail(email);
        loginDTO.setPassword(password);
        loginCommand = new LoginCommand(this, loginDTO, userRepository );
        return loginCommand.execute();
    }

    // Logout
    @CacheEvict(value = "users", key = "#userId")
    public ResponseEntity<?> logout(Long userId) {
        logoutCommand = new LogoutCommand(this, userId, userRepository);
        return logoutCommand.execute();
    }

    // Change Password
    @CacheEvict(value = "users", key = "#userId") // make it cachePut
    public ResponseEntity<?> changePassword(Long userId, String newPassword) {
        changePasswordCommand = new ChangePasswordCommand(this, userId, newPassword, userRepository);
        return changePasswordCommand.execute();
    }

    // Get user by ID
  //  @Cacheable (value = "users", key = "#userId")
    public User getUserById(Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    @CacheEvict(value = "users", key = "#userId")
    public ResponseEntity<?> deleteUser(Long userId) {
        deleteUserCommand = new DeleteUserCommand(this, userId, userRepository);
        return deleteUserCommand.execute();
    }


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


    public ResponseEntity<?> updateUserProfileWithMessage(Long userId, UserProfile updatedProfile) {
        try {
            UserProfile updated = updateUserProfile(userId, updatedProfile);
            return ResponseEntity.ok("User profile updated successfully:\n" + updated.toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    public void processBookingNotification(Long userId, String bookingNotification) {
        // 1. Verify user exists (optional check)
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        // 2. Process the notification (just print it in this simple version)
        System.out.println("Booking notification for user " + userId + ": " + bookingNotification);
    }

    // View Past Flights
//    @Cacheable(value = "pastFlights", key = "#userId")
//    public List<PastFlightDTO> viewPastFlights(Long userId) {
//        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
//            throw new RuntimeException("User is not logged in.");
//        }
//
//        String url = bookingServiceUrl + "/bookings/past-flights/" + userId;
//
//        try {
//            return Arrays.asList(response);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve past flights: " + e.getMessage());
//        }
//    }
//
//    // View Past Flights
//    public List<PastFlightDTO> viewPastFlights(Long userId) {
//        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
//            throw new RuntimeException("User is not logged in.");
//        }
//
//       String url = bookingServiceUrl + "/bookings/past-flights/" + userId;
//
//
//        try {
//            PastFlightDTO[] response = restTemplate.getForObject(url, PastFlightDTO[].class);
//            return Arrays.asList(response);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to retrieve past flights: " + e.getMessage());
//        }
//    }

    // Get user by full name
    @Cacheable(value = "usersByFullName", key = "#fullName")
    public User getUserByFullName(String fullName) {
        return userRepository.findByFullName(fullName)
                .orElseThrow(() -> new RuntimeException("User not found with full name: " + fullName));
    }

    // Get user by email
    @Cacheable(value = "usersByEmail", key = "#email")
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

}