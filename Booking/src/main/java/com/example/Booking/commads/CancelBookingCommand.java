package com.example.Booking.commads;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.UUID;

@Getter @RequiredArgsConstructor
public class CancelBookingCommand {
    private final UUID bookingId;
}
