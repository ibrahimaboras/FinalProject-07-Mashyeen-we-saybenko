package com.example.Booking.commads;// AddFlightTicketCommand.java


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/** Attach a new FlightTicket to an existing Booking. */
@Getter @RequiredArgsConstructor
public class AddFlightTicketCommand {
    private final UUID bookingId;
    private final String fullName;
    private final String nationality;
    private final String passportNumber;
    private final String gender;
    private final LocalDate dateOfBirth;
    private final UUID flightId;
    private final UUID seatId;
}
