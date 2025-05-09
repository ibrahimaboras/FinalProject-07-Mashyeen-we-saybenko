
package com.example.Booking.Events;

import lombok.Getter;
import java.util.UUID;

@Getter
public class BookingCancelledEvent {
    private final UUID bookingId;

    public BookingCancelledEvent(UUID bookingId) {
        this.bookingId = bookingId;
    }
}
