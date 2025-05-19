package com.example.Notification.listener;

import lombok.Data;

@Data
public class FlightTicketDto {
    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private String dateOfBirth;
    private String flightId;
    private String seatId;
    private String seatClass;
}