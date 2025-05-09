package com.example.Booking.commads;// src/main/java/com/example/Booking/command/CancelBookingCommand.java



import com.example.Booking.service.BookingService;

import java.util.UUID;

/**
 * Encapsulates “cancel a booking” as an object.
 */
public class CancelBookingCommand implements BookingCommand {
    private final BookingService bookingService;
    private final UUID bookingId;

    public CancelBookingCommand(BookingService bookingService, UUID bookingId) {
        this.bookingService = bookingService;
        this.bookingId      = bookingId;
    }

    @Override
    public void execute() {
        bookingService.cancelBooking(bookingId);
    }
}
