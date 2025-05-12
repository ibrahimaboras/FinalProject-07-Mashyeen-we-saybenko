// src/main/java/com/example/Booking/Events/BookingCreatedEvent.java
package com.example.Booking.Events;

import lombok.Getter;

import java.util.UUID;

/**
 * Published when a new Booking is created (but before payment confirmation).
 */
@Getter
public class BookingCreatedEvent {
    private final UUID bookingId;
    private final UUID userId;

    public BookingCreatedEvent(UUID bookingId, UUID userId) {
        this.bookingId = bookingId;
        this.userId    = userId;
    }
}
