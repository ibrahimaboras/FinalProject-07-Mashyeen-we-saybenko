package com.example.Booking.controller;

import com.example.Booking.model.FlightTicket;
import com.example.Booking.service.FlightTicketService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tickets")
public class FlightTicketController {
    private final FlightTicketService service;

    public FlightTicketController(FlightTicketService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public FlightTicket get(@PathVariable UUID id) {
        return service.getTicket(id);
    }

    @GetMapping
    public List<FlightTicket> listByBooking(@RequestParam UUID bookingId) {
        return service.getTicketsByBooking(bookingId);
    }
}
