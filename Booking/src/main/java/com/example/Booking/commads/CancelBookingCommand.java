package com.example.Booking.commads;// CancelBookingCommand.java


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

/** Mark a booking as cancelled. */
@Getter @RequiredArgsConstructor
public class CancelBookingCommand {
    private final UUID bookingId;
}
