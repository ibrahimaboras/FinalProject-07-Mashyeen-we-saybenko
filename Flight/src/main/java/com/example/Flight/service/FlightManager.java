package com.example.Flight.service;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Flight.repository.FlightRepository;
import com.example.Flight.repository.SeatRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightManager {
    private static volatile FlightManager instance;
    
    @Autowired
    private FlightRepository flightRepository;
    
    @Autowired
    private SeatRepository seatRepository;

    private FlightManager() {}

    public static FlightManager getInstance() {
        if (instance == null) {
            synchronized (FlightManager.class) {
                if (instance == null) {
                    instance = new FlightManager();
                }
            }
        }
        return instance;
    }

    // CRUD Operations that respect immutability

    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> updateFlight(Long flightId, Flight updatedFlight) {
        return flightRepository.findById(flightId)
            .map(existingFlight -> {
                // Create a new flight with the updated values
                Flight toSave = updatedFlight.copyBuilder()
                    .build();
                toSave.setFlightId(flightId); // Preserve ID
                return flightRepository.save(toSave);
            });
    }

    public void deleteFlight(Long id) {
        flightRepository.deleteById(id);
    }

    // Additional functionality

    public List<Flight> filterFlightsByDestinationAndDate(
            String destination, 
            LocalDateTime startDate, 
            LocalDateTime endDate) {
        return flightRepository.findByDestinationAndDepartureTimeBetween(
            destination, startDate, endDate);
    }

    public boolean checkSeatAvailability(Long flightId, String seatNumber) {
        return flightRepository.findById(flightId)
            .map(flight -> {
                Seat seat = seatRepository.findBySeatNumberAndFlight(seatNumber, flight);
                return seat != null && seat.getIsAvailable();
            })
            .orElse(false);
    }

    public Optional<Integer> getAvailableSeatCount(Long flightId) {
        return flightRepository.findById(flightId)
            .map(flight -> seatRepository.findByFlightAndIsAvailable(flight, true).size());
    }

    public Optional<Flight> updateFlightStatus(Long flightId, String newStatus) {
        return flightRepository.findById(flightId)
            .map(existingFlight -> {
                Flight updated = existingFlight.copyBuilder()
                    .status(newStatus)
                    .build();
                return flightRepository.save(updated);
            });
    }

    public Optional<Flight> reserveSeat(Long flightId, String seatNumber) {
        return flightRepository.findById(flightId)
            .flatMap(flight -> {
                Seat seat = seatRepository.findBySeatNumberAndFlight(seatNumber, flight);
                if (seat != null && seat.getIsAvailable()) {
                    // Update seat availability
                    seat.setIsAvailable(false);
                    seatRepository.save(seat);
                    
                    // Create and return updated flight with new available seats count
                    Flight updatedFlight = flight.copyBuilder()
                        .availableSeats(flight.getAvailableSeats() - 1)
                        .build();
                    return Optional.of(flightRepository.save(updatedFlight));
                }
                return Optional.empty();
            });
    }

    public List<Flight> getFlightsByStatus(String status) {
        return flightRepository.findByStatus(status);
    }
}
