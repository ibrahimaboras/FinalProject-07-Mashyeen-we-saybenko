package com.example.Flight.controller;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Price;
import com.example.Flight.service.FlightManager;
import com.example.Flight.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;
    private final FlightManager flightManager;

    @Autowired
    public FlightController(FlightService flightService, FlightManager flightManager) {
        this.flightService = flightService;
        this.flightManager = flightManager;
    }

    // Basic CRUD Operations

    @PostMapping
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightService.createFlight(flight);
        return ResponseEntity.ok(createdFlight);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Flight flight = flightService.getFlightById(id);
        return flight != null ? ResponseEntity.ok(flight) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable Long id, @RequestBody Flight flight) {
        Flight updatedFlight = flightService.updateFlight(id, flight);
        return updatedFlight != null ? ResponseEntity.ok(updatedFlight) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        boolean deleted = flightService.deleteFlight(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Advanced Flight Operations

    @GetMapping
    public List<Flight> getAllFlights() {
        return flightManager.getAllFlights();
    }

    @GetMapping("/search/originAndDestination")
    public List<Flight> searchFlightsByOriginAndDestination(
            @RequestParam String origin,
            @RequestParam String destination) {
        return flightService.filterFlightsByOriginAndDestination(origin, destination);
    }

    @GetMapping("/search/destinationAndDate")
    public List<Flight> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {
        return flightService.filterFlightsByDestinationAndDate(origin, destination, startTime, endTime);
    }

    @GetMapping("/status/{status}")
    public List<Flight> getFlightsByStatus(@PathVariable String status) {
        return flightManager.getFlightsByStatus(status);
    }

    @GetMapping("/{id}/seats/available")
    public ResponseEntity<Integer> getAvailableSeatCount(@PathVariable Long id) {
        Optional<Integer> seatCount = flightManager.getAvailableSeatCount(id);
        return seatCount.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/seats/check")
    public ResponseEntity<Boolean> checkSeatAvailability(
            @PathVariable Long id,
            @RequestParam String seatNumber) {
        boolean available = flightManager.checkSeatAvailability(id, seatNumber);
        return ResponseEntity.ok(available);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Flight> updateFlightStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Optional<Flight> updatedFlight = flightManager.updateFlightStatus(id, status);
        return updatedFlight.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/seats/reserve")
    public ResponseEntity<Flight> reserveSeat(
            @PathVariable Long id,
            @RequestParam String seatNumber) {
        Optional<Flight> updatedFlight = flightManager.reserveSeat(id, seatNumber);
        return updatedFlight.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    // Pricing Related Endpoints

    @GetMapping("/sorted/price")
    public List<Flight> getFlightsSortedByPrice(
            @RequestParam String origin,
            @RequestParam String destination) {
        return flightService.getFlightsSortedByMinPrice(origin, destination);
    }

    @GetMapping("/sorted/price/{classType}")
    public List<Flight> getFlightsSortedByClassPrice(@PathVariable String classType) {
        return flightManager.getFlightsSortedByClassPrice(classType);
    }

    @GetMapping("/{id}/price/min")
    public ResponseEntity<Double> getMinPriceForFlight(@PathVariable Long id) {
        Double minPrice = flightManager.getMinPriceForFlight(id);
        return ResponseEntity.ok(minPrice);
    }

    @GetMapping("/{id}/price/min/{classType}")
    public ResponseEntity<Double> getMinPriceForFlightAndClass(
            @PathVariable Long id,
            @PathVariable String classType) {
        Double minPrice = flightManager.getMinPriceForFlightAndClass(id, classType);
        return ResponseEntity.ok(minPrice);
    }

    @GetMapping("/{id}/seats/prices")
    public ResponseEntity<List<Price>> getAvailableSeatsWithPrices(@PathVariable Long id) {
        List<Price> seatsWithPrices = flightManager.getAvailableSeatsSortedByPrice(id);
        return ResponseEntity.ok(seatsWithPrices);
    }

    // // Additional Utility Endpoints

    // @GetMapping("/count")
    // public long countFlightsByRoute(
    //         @RequestParam String origin,
    //         @RequestParam String destination) {
    //     return flightManager.countFlightsByRoute(origin, destination);
    // }

    // @GetMapping("/aircraft/{model}")
    // public List<Flight> getFlightsByAircraftModel(@PathVariable String model) {
    //     return flightManager.getFlightsByAircraftModel(model);
    // }

    // @GetMapping("/departing-after")
    // public List<Flight> getFlightsDepartingAfter(
    //         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureTime) {
    //     return flightManager.getFlightsDepartingAfter(departureTime);
    // }
}