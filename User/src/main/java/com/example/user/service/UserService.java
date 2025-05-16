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
                       UserProfileRepository userProfileRepository,
                       RestTemplate restTemplate) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.restTemplate = restTemplate;
    }

    // Register User
    public User registerUser(User user) {
        createUserCommand = new CreateUserCommand(this, user, this.userRepository);
        return createUserCommand.execute();
    }

    // Login
    //Consider for better security not giving them a direct answer of whether it is the email or password??
    public User login(String email, String password) {
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
    public String logout(Long userId) {
        logoutCommand = new LogoutCommand(this, userId, userRepository);
        return logoutCommand.execute();
    }

    // Change Password
    public void changePassword(Long userId, String newPassword) {
        changePasswordCommand = new ChangePasswordCommand(this, userId, newPassword, userRepository);
        changePasswordCommand.execute();
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // Delete user
    public void deleteUser(Long userId) {
        deleteUserCommand = new DeleteUserCommand(this, userId, userRepository);
        deleteUserCommand.execute();
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
    // Get user by username (full name)
    public User getUserByFullName(String fullName) {
        return userRepository.findByFullName(fullName)
                .orElseThrow(() -> new RuntimeException("User not found with full name: " + fullName));
    }

    // Get user by email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }


}
