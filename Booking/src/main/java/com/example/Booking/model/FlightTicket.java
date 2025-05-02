package com.example.Booking.model;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDate;

@Entity
public class FlightTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID flightTicketId;

    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private LocalDate dateOfBirth;

    // Relationships
    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public UUID getFlightTicketId() {
        return flightTicketId;
    }

    public void setFlightTicketId(UUID flightTicketId) {
        this.flightTicketId = flightTicketId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public UUID getFlightId() {
        return flightId;
    }

    public void setFlightId(UUID flightId) {
        this.flightId = flightId;
    }

    public UUID getSeatId() {
        return seatId;
    }

    public void setSeatId(UUID seatId) {
        this.seatId = seatId;
    }

    @Column(nullable = false)
    private UUID flightId;

    @Column(nullable = false)
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
