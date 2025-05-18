package com.example.Booking.controller;



import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;

import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final CommandGateway gateway;
    private final BookingService service;

    public BookingController(CommandGateway gateway,
                             BookingService service) {
        this.gateway = gateway;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateBookingCommand cmd) {
        gateway.send(cmd);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public Booking get(@PathVariable UUID id) {
        return service.getBooking(id);
    }

    @GetMapping
    public List<Booking> list(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) BookingStatus status
    ) {
        if (userId != null) return service.getBookingsByUser(userId);
        if (status != null) return service.getBookingsByStatus(status);
        return service.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        gateway.send(new CancelBookingCommand(id));
        return ResponseEntity.accepted().build();
    }
}
