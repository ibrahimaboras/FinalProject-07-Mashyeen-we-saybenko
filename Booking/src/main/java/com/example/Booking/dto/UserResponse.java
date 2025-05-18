package com.example.Booking.dto;  // Or your preferred package

import lombok.Data;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID userId;
    private String fullName;
    private String email;
    private String phone;
    // Add other fields as needed
}