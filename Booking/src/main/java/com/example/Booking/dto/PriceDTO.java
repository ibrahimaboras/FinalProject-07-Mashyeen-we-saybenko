package com.example.Booking.dto;

public class PriceDTO {

    private Long id;
    private FlightDTO flight;
    private SeatDTO seat;
    private Double price;

    // Default constructor
    public PriceDTO() {
    }

    // Parameterized constructor
    public PriceDTO(Long id, FlightDTO flight, SeatDTO seat, Double price) {
        this.id = id;
        this.flight = flight;
        this.seat = seat;
        this.price = price;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FlightDTO getFlight() {
        return flight;
    }

    public void setFlight(FlightDTO flight) {
        this.flight = flight;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
