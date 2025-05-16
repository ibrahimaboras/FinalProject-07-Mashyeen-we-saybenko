package com.example.Booking.commads;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CancelBookingCommand implements Command, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID bookingId;
}