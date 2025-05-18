package com.example.Booking.model;// com/example/booking/model/FlightTicket.java

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatClass seatClass;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference
    private Booking booking;
    private UUID flightId;
    private UUID seatId;

    public FlightTicket() { }

    public FlightTicket(String fullName,
                        String nationality,
                        String passportNumber,
                        String gender,
                        LocalDate dateOfBirth,
                        SeatClass seatClass,
                        Booking booking,
                        UUID flightId,
                        UUID seatId) {
        this.fullName = fullName;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.seatClass = seatClass;
        this.booking = booking;
        this.flightId = flightId;
        this.seatId = seatId;
    }
}
