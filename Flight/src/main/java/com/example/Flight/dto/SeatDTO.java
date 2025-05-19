package com.example.Flight.dto;

public class SeatDTO {

    private Long seatId;
    private String seatNumber;
    private Boolean isAvailable;
    private String classType;

    // Default constructor
    public SeatDTO() {
    }

    // Parameterized constructor
    public SeatDTO(Long seatId, String seatNumber, Boolean isAvailable, String classType) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
        this.classType = classType;
    }

    // Getters and Setters
    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }
}

