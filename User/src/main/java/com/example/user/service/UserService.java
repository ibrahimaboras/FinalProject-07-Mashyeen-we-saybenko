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



import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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


    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    @Autowired
    public UserService(UserRepository userRepository,
                       UserProfileRepository userProfileRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    // Register User
    public  ResponseEntity<?> registerUser(User user) {
        createUserCommand = new CreateUserCommand(this, user, this.userRepository);
        return createUserCommand.execute();
    }

    // Login
    //Consider for better security not giving them a direct answer of whether it is the email or password??
    public ResponseEntity<?> login(String email, String password) {
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isEmpty()) {
//            throw new RuntimeException("User not found");
//        }
//
//        User user = optionalUser.get();
//        if (!user.getPassword().equals(password)) {
//            throw new RuntimeException("Incorrect password");
//        }
//
//        SingletonSessionManager.getInstance().startSession(user.getUserId());
//        return user;
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
    @Cacheable (value = "users", key = "#userId")
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

//    // Update Profile
//    @CachePut(value = "userProfiles", key = "#userId") // Update the UserProfile cache
//    public UserProfile updateUserProfile(Long userId, UserProfile updatedProfile) {
//        if (!SingletonSessionManager.getInstance().isLoggedIn(userId)) {
//            throw new RuntimeException("User is not logged in.");
//        }
//
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        UserProfile profile = user.getProfile();
//
//        if (profile == null) {
//            updatedProfile.setUser(user);
//            return userProfileRepository.save(updatedProfile);
//        } else {
//            profile.setNationality(updatedProfile.getNationality());
//            profile.setPassportNumber(updatedProfile.getPassportNumber());
//            profile.setGender(updatedProfile.getGender());
//            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
//            return userProfileRepository.save(profile);
//        }
//    }

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