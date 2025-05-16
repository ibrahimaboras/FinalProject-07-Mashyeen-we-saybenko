package com.example.Flight.model;

import com.example.Flight.model.Flight;

import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @Column(name = "class_type", nullable = false)
    private String classType;

    // Default constructor
    public Seat() {
    }

    // Parameterized constructor
    public Seat(Flight flight, String seatNumber, Boolean isAvailable, String classType) {
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
        this.classType = classType;
    }

    // Constructor for updating
    public Seat(Long id, Flight flight, String seatNumber, Boolean isAvailable, String classType) {
        this.id = id;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.isAvailable = isAvailable;
        this.classType = classType;
    }

    // Getters and Setters
    public Long getSeatId() {
        return id;
    }

    public void setSeatId(Long seatId) {
        this.id = seatId;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
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
