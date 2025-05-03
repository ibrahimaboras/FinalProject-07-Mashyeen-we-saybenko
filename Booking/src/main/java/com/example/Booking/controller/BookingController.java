package com.example.Booking.controller;


import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse getBooking(@PathVariable UUID bookingId) {
        return BookingResponse.from(bookingService.getBooking(bookingId));
    }

    @DeleteMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable UUID bookingId) {
        bookingService.cancelBooking(bookingId);
    }
}