package com.example.Flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Flight.model.Aircraft;

@Repository
public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    // Custom query methods can be added here
    // Example:
    // List<Aircraft> findByAirlineName(String airlineName);
}
