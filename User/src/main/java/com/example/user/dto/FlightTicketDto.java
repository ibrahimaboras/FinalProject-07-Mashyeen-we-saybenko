package com.example.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

// you can use java.time.LocalDate for dateOfBirth; Jackson will handle it.
@Getter
public class FlightTicketDto {
    private String fullName;
    private String nationality;
    private String passportNumber;
    private String gender;
    private String dateOfBirth;
    private String flightId;
    private String seatId;
    private String seatClass;

    public FlightTicketDto() {}

    public void setFullName(String fullName) { this.fullName = fullName; }

    public void setNationality(String nationality) { this.nationality = nationality; }

    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public void setGender(String gender) { this.gender = gender; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public void setFlightId(String flightId) { this.flightId = flightId; }

    public void setSeatId(String seatId) { this.seatId = seatId; }

    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }
}
