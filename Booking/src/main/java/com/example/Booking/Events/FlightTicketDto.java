package com.example.Booking.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// you can use java.time.LocalDate for dateOfBirth; Jackson will handle it.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlightTicketDto {
    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private String dateOfBirth;  // e.g. "1988-11-03"
    private String flightId;
    private String seatId;
    private String seatClass;
}