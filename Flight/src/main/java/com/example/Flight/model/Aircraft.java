package com.example.Flight.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aircraft")
public class Aircraft {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aircraft_id")
    private Long id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer capacity;

    @Column(name = "airline_name", nullable = false)
    private String airlineName;

    // Getters and Setters
    public Long getAircraftId() {
        return id;
    }

    public void setAircraftId(Long aircraftId) {
        this.id = aircraftId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }
}