
package com.example.Booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BookingRequest {
    @NotNull
    private UUID userId;

    @NotNull @Size(min = 1)
    private List<FlightTicketRequest> tickets;

    /** Now reusing the same DTO as your payment endpoint */
    @NotNull
    private PaymentRequest paymentDetails;
}
