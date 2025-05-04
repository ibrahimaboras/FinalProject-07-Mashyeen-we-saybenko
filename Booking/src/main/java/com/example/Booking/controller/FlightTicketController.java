package com.example.Booking.controller;

import com.example.Booking.model.FlightTicket;
import com.example.Booking.service.FlightTicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flight-tickets")
public class FlightTicketController {

    private final FlightTicketService flightTicketService;

    public FlightTicketController(FlightTicketService flightTicketService) {
        this.flightTicketService = flightTicketService;
    }

    @PostMapping("/{bookingId}")
    public ResponseEntity<FlightTicket> createFlightTicket(
            @PathVariable UUID bookingId,
            @RequestBody FlightTicket ticket) {
        FlightTicket created = flightTicketService.createFlightTicket(bookingId, ticket);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/by-booking/{bookingId}")
    public ResponseEntity<List<FlightTicket>> getTicketsByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(flightTicketService.getTicketsByBooking(bookingId));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable UUID ticketId) {
        flightTicketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }
}
