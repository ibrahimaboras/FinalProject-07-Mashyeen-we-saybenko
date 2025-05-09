package com.example.Booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PaymentRequest {
    @NotNull
    private UUID bookingId;

    @Positive
    private BigDecimal amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String paymentMethod;  // e.g., "CREDIT_CARD"
}