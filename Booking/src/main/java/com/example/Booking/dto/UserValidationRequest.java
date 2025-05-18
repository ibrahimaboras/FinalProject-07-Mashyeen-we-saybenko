package com.example.Booking.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class UserValidationRequest {
    private UUID userId;
}