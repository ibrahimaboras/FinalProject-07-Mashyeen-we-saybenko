package com.example.Booking.commads;// CreateBookingCommand.java


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * Create a new Booking for a user, optionally
 * with an initial list of tickets.
 */
@Getter @RequiredArgsConstructor
public class CreateBookingCommand {
    private final UUID userId;
    private final List<InitialTicket> tickets;

    @Getter @RequiredArgsConstructor
    public static class InitialTicket {
        private final String fullName;
        private final String nationality;
        private final String passportNumber;
        private final String gender;
        private final java.time.LocalDate dateOfBirth;
        private final UUID flightId;
        private final UUID seatId;
    }
}
