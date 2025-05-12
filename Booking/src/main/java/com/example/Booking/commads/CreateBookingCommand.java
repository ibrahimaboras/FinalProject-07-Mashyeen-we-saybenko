package com.example.Booking.commads;


import com.example.Booking.commads.BookingCommand;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.service.BookingService;

/**
 * Encapsulates “create a booking” as an object.
 */
public class CreateBookingCommand implements BookingCommand {
    private final BookingService bookingService;
    private final BookingRequest request;

    public CreateBookingCommand(BookingService bookingService, BookingRequest request) {
        this.bookingService = bookingService;
        this.request        = request;
    }

    @Override
    public void execute() {
        bookingService.createBooking(request);
    }
}
