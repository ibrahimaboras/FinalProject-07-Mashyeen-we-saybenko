package com.example.Booking.controller;// src/main/java/com/example/Booking/controller/BookingController.java


import com.example.Booking.commads.BookingCommandInvoker;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingCommandInvoker invoker;
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createBooking(@Valid @RequestBody BookingRequest request) {
        invoker.addCommand(new CreateBookingCommand(bookingService, request));
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void cancelBooking(@PathVariable UUID bookingId) {
        invoker.addCommand(new CancelBookingCommand(bookingService, bookingId));
    }
}
