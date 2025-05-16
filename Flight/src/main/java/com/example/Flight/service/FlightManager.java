package com.example.Flight.service;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Price;
import com.example.Flight.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Flight.repository.FlightRepository;
import com.example.Flight.repository.PriceRepository;
import com.example.Flight.repository.SeatRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightManager {
    private static volatile FlightManager instance;
    
    @Autowired
    private FlightRepository flightRepository;
    
    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PriceRepository priceRepository;

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

    public List<Flight> filterFlightsByOriginAndDestination(
            String origin, 
            String destination) {
        return flightRepository.findByOriginAndDestination(origin, destination);
    }

    public List<Flight> filterFlightsByDestinationAndDate(
            String origin,
            String destination,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime) {
        return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                origin, destination, startDateTime, endDateTime);
    }

    public boolean checkSeatAvailability(Long flightId, String seatNumber) {
        return flightRepository.findById(flightId)
            .map(flight -> {
                Seat seat = seatRepository.findBySeatNumberAndFlightId(seatNumber, flight.getFlightId());
                return seat != null && seat.getIsAvailable();
            })
            .orElse(false);
    }

    public Optional<Integer> getAvailableSeatCount(Long flightId) {
        return flightRepository.findById(flightId)
            .map(flight -> seatRepository.findByFlightIdAndIsAvailable(flight.getFlightId(), true).size());
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
                Seat seat = seatRepository.findBySeatNumberAndFlightId(seatNumber, flight.getFlightId());
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

    /**
     * Get flights sorted by minimum available price
     */
    public List<Flight> getFlightsSortedByMinPrice(
            String origin, 
            String destination)  {
        List<Flight> flights = filterFlightsByOriginAndDestination(origin, destination);
        return priceRepository.findMinPricesForFlights(flights).stream()
            .map(result -> (Flight) result[0])
            .collect(Collectors.toList());
    }
    
    /**
     * Get flights sorted by specific seat class price
     * @param classType - the class type to sort by (e.g., "Economy", "Business")
     */
    public List<Flight> getFlightsSortedByClassPrice(String classType) {
        return flightRepository.findAll().stream()
            .sorted(Comparator.comparing(flight -> {
                Double minPrice = priceRepository
                    .findByFlightIdAndSeatClassType(flight.getFlightId(), classType)
                    .stream()
                    .mapToDouble(Price::getPrice)
                    .min()
                    .orElse(Double.MAX_VALUE);
                return minPrice;
            }))
            .collect(Collectors.toList());
    }
    
    /**
     * Get available seats for a flight sorted by price
     */
    public List<Price> getAvailableSeatsSortedByPrice(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new RuntimeException("Flight not found"));
        return priceRepository.findAvailablePricesByFlight(flight);
    }

    public List<Flight> getFlightsByStatus(String status) {
        return flightRepository.findByStatus(status);
    }

    public Double getMinPriceForFlight(Long flightId) {
        return priceRepository.findByFlightId(flightId).stream()
            .mapToDouble(Price::getPrice)
            .min()
            .orElse(0.0);
    }
    
    public Double getMinPriceForFlightAndClass(Long flightId, String classType) {
        return priceRepository.findByFlightIdAndSeatClassType(flightId, classType).stream()
            .mapToDouble(Price::getPrice)
            .min()
            .orElse(0.0);
    }
    
}
