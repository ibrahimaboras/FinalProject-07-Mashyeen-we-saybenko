package com.example.Booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.time.LocalDate;



@Setter
@Entity
@Table(name = "flight_tickets")
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID flightTicketId;

    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private UUID flightId;
    private UUID seatId;

    // Constructors
    public FlightTicket() {}

    public FlightTicket(String fullName, String nationality, String passportNumber,
                        String gender, LocalDate dateOfBirth, Booking booking,
                        UUID flightId, UUID seatId) {
        this.fullName = fullName;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.booking = booking;
        this.flightId = flightId;
        this.seatId = seatId;
    }

}
