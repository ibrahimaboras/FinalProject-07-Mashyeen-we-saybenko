package com.example.Booking.commads;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        private final LocalDate dateOfBirth;
        private final UUID flightId;
        private final UUID seatId;
        private final String seatClass;
    }
}
