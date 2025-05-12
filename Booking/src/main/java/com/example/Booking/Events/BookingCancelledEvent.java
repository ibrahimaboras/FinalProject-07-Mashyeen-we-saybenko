package com.example.Booking.Events;

import java.util.UUID;

public class BookingCancelledEvent {
    private final UUID bookingId;

    public BookingCancelledEvent(UUID bookingId) {
        this.bookingId = bookingId;
    }

    public UUID getBookingId() { return bookingId; }
}