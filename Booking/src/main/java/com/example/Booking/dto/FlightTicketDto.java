// src/main/java/com/example/Booking/dto/FlightTicketDto.java
package com.example.Booking.dto;

import com.example.Booking.model.FlightTicket;

import java.time.LocalDate;
import java.util.UUID;

public class FlightTicketDto {
    private UUID flightTicketId;
    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private LocalDate dateOfBirth;
    private UUID flightId;
    private UUID seatId;

    public static FlightTicketDto from(FlightTicket t) {
        FlightTicketDto d = new FlightTicketDto();
        d.flightTicketId = t.getFlightTicketId();
        d.fullName       = t.getFullName();
        d.nationality    = t.getNationality();
        d.passportNumber = t.getPassportNumber();
        d.gender         = t.getGender();
        d.dateOfBirth    = t.getDateOfBirth();
        d.flightId       = t.getFlightId();
        d.seatId         = t.getSeatId();
        return d;
    }
    // getters...
}
