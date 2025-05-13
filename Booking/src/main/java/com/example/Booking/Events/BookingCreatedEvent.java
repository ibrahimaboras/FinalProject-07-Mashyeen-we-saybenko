package com.example.Booking.Events;// src/main/java/com/example/booking/events/BookingCreatedEvent.java


import java.util.UUID;

public class BookingCreatedEvent {
    private final UUID bookingId;
    private final UUID userId;

    public BookingCreatedEvent(UUID bookingId, UUID userId) {
        this.bookingId = bookingId;
        this.userId = userId;
    }

    public UUID getBookingId() { return bookingId; }
    public UUID getUserId()    { return userId;    }
}
