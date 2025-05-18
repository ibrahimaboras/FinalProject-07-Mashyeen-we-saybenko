package com.example.Flight.controller;

import com.example.Flight.model.Seat;
import com.example.Flight.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seat> getSeatById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(seatService.getSeatById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/flight/{flightId}")
    public List<Seat> getSeatsByFlight(@PathVariable Long flightId) {
        return seatService.getSeatsByFlight(flightId);
    }

    @GetMapping("/flight/{flightId}/available")
    public List<Seat> getAvailableSeatsByFlight(@PathVariable Long flightId) {
        return seatService.getAvailableSeatsByFlight(flightId);
    }

    @GetMapping("/flight/{flightId}/class/{classType}")
    public List<Seat> getSeatsByFlightAndClass(
            @PathVariable Long flightId,
            @PathVariable String classType) {
        return seatService.getSeatsByFlightAndClass(flightId, classType);
    }

    @PostMapping
    public ResponseEntity<?> createSeat(@RequestBody Seat seat) {
        try {
            return ResponseEntity.ok(seatService.createSeat(seat));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Long id, @RequestBody Seat seatDetails) {
        try {
            return ResponseEntity.ok(seatService.updateSeat(id, seatDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Seat> updateSeatAvailability(
            @PathVariable Long id,
            @RequestParam Boolean isAvailable) {
        try {
            return ResponseEntity.ok(seatService.updateSeatAvailability(id, isAvailable));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        try {
            seatService.deleteSeat(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}