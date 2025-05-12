package com.example.Booking.controller;

import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public Booking create(@RequestBody CreateBookingCommand cmd) {
        return bookingService.create(cmd);
    }

    @GetMapping("/{id}")
    public Booking get(@PathVariable UUID id) {
        return bookingService.getById(id);
    }

    @DeleteMapping("/{id}")
    public Booking cancel(@PathVariable UUID id) {
        return bookingService.cancel(new CancelBookingCommand(id));
    }
}
