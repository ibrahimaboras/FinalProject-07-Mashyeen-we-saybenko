package com.example.Booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Data
public class BookingRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private List<FlightTicketRequest> tickets;

    @NotNull
    private PaymentDetails paymentDetails;
}