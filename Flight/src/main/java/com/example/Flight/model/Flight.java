package com.example.Flight.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private Aircraft aircraft;

    private String origin;
    private String destination;
    private String departureTime;
    private String arrivalTime;

    private String status; // e.g., "Scheduled", "Delayed", etc.
    private String classType; // e.g., "Economy", "Business"
    private int availableSeats;
    private String gateInfo;

    public Flight() {
        // Default constructor
    }

    private Flight(Builder builder) {
        this.aircraft = builder.aircraft;
        this.origin = builder.origin;
        this.destination = builder.destination;
        this.departureTime = builder.departureTime;
        this.arrivalTime = builder.arrivalTime;
        this.status = builder.status;
        this.classType = builder.classType;
        this.availableSeats = builder.availableSeats;
        this.gateInfo = builder.gateInfo;
    }

    public void setFlightId(Long flightId) {
        this.id = flightId;
    }

    // Getters
    public Long getFlightId() {
        return id;
    }

    public Aircraft getAircraft() {
        return aircraft;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getStatus() {
        return status;
    }

    public String getClassType() {
        return classType;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public String getGateInfo() {
        return gateInfo;
    }

    // Builder pattern for creating Flight instances
    public static class Builder {
        private Aircraft aircraft;
        private String origin;
        private String destination;
        private String departureTime;
        private String arrivalTime;
        private String status;
        private String classType;
        private int availableSeats;
        private String gateInfo;
    
        public Builder aircraft(Aircraft aircraft) {
            this.aircraft = aircraft;
            return this;
        }
    
        public Builder origin(String origin) {
            this.origin = origin;
            return this;
        }
    
        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }
    
        public Builder departureTime(String departureTime) {
            this.departureTime = departureTime;
            return this;
        }
    
        public Builder arrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }
    
        public Builder status(String status) {
            this.status = status;
            return this;
        }
    
        public Builder classType(String classType) {
            this.classType = classType;
            return this;
        }
    
        public Builder availableSeats(int availableSeats) {
            this.availableSeats = availableSeats;
            return this;
        }
    
        public Builder gateInfo(String gateInfo) {
            this.gateInfo = gateInfo;
            return this;
        }
    
        public Flight build() {
            return new Flight(this);
        }
    }

     // Copy builder for updates
     public Builder copyBuilder() {
        return new Builder()
            .aircraft(this.aircraft)
            .origin(this.origin)
            .destination(this.destination)
            .departureTime(this.departureTime)
            .arrivalTime(this.arrivalTime)
            .status(this.status)
            .classType(this.classType)
            .availableSeats(this.availableSeats)
            .gateInfo(this.gateInfo);
    }
}
