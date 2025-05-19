package com.example.user;

import java.time.LocalDate;
import java.util.UUID;

public class FlightTicketDTO {
    private UUID flightTicketId;

    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private LocalDate dateOfBirth;

    private Long flightId;
    private Long seatId;
    private Long priceId;

    // Constructors
    public FlightTicketDTO() {}

    public FlightTicketDTO(UUID flightTicketId, String fullName, String nationality, String passportNumber, String gender,
                           LocalDate dateOfBirth,
                           Long flightId, Long seatId, Long priceId) {
        this.flightTicketId = flightTicketId;
        this.fullName = fullName;
        this.nationality = nationality;
        this.passportNumber = passportNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.flightId = flightId;
        this.seatId = seatId;
        this.priceId = priceId;
    }

    // Getters and Setters
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

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }
}
