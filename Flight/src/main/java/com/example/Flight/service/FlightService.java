package com.example.Flight.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.Flight.model.Flight;
import com.example.Flight.repository.FlightRepository;
import com.example.Flight.repository.PriceRepository;

@Service
public class FlightService {
    // In a real app, this would be a database repository (e.g., JPA, MongoDB)
    private final FlightRepository flightRepository; 

    @Autowired
    private PriceRepository priceRepository;

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
    @Cacheable(value = "flightsByOriginDestination", key = "#origin + '-' + #destination")
    public List<Flight> filterFlightsByOriginAndDestination(
            String origin, 
            String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }
    @Cacheable(
            value = "flightsByOriginDestinationDate",
            key = "#origin + '-' + #destination + '-' + #startDateTime.toString() + '-' + #endDateTime.toString()"
    )
    public List<Flight> filterFlightsByDestinationAndDate(
            String origin,
            String destination,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                origin, destination, startDateTime, endDateTime);
    }
    @Cacheable(value = "flightsByPrice", key = "#origin + '-' + #destination")
    public List<Flight> getFlightsSortedByMinPrice(
            String origin, 
            String destination)  {
        List<Flight> flights = filterFlightsByOriginAndDestination(origin, destination);
        return priceRepository.findMinPricesForFlights(flights).stream()
            .map(result -> (Flight) result[0])
            .collect(Collectors.toList());
    }
}