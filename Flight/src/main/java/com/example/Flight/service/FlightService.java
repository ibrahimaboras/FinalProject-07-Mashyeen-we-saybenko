package com.example.Flight.service;

import org.springframework.stereotype.Service;

import com.example.Flight.model.Flight;
import com.example.Flight.repository.FlightRepository;

@Service
public class FlightService {
    // In a real app, this would be a database repository (e.g., JPA, MongoDB)
    private final FlightRepository flightRepository; 

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    // Create a single flight using the builder pattern
    public Flight createFlight(Flight flight) {
        Flight newFlight = new Flight.Builder()
            .aircraft(flight.getAircraft())
            .origin(flight.getOrigin())
            .destination(flight.getDestination())
            .departureTime(flight.getDepartureTime())
            .arrivalTime(flight.getArrivalTime())
            .status(flight.getStatus())
            .classType(flight.getClassType())
            .availableSeats(flight.getAvailableSeats())
            .gateInfo(flight.getGateInfo())
            .build();

        return flightRepository.save(newFlight);
    }


    // Get a single flight by ID
    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId).orElse(null);
    }

    // Update a single flight (immutable approach)
    public Flight updateFlight(Long flightId, Flight updatedFlight) {
        Flight existingFlight = getFlightById(flightId);
        if (existingFlight != null) {
            // Use Builder to preserve immutability
            Flight newFlight = new Flight.Builder()
                .aircraft(updatedFlight.getAircraft())
                .origin(updatedFlight.getOrigin())
                .destination(updatedFlight.getDestination())
                .departureTime(updatedFlight.getDepartureTime())
                .arrivalTime(updatedFlight.getArrivalTime())
                .status(updatedFlight.getStatus())
                .classType(updatedFlight.getClassType())
                .availableSeats(updatedFlight.getAvailableSeats())
                .gateInfo(updatedFlight.getGateInfo())
                .build();
            
            newFlight.setFlightId(flightId); // Preserve ID
            return flightRepository.save(newFlight);
        }
        return null;
    }

    // Delete a single flight
    public boolean deleteFlight(Long flightId) {
        if (flightRepository.existsById(flightId)) {
            flightRepository.deleteById(flightId);
            return true;
        }
        return false;
    }
}