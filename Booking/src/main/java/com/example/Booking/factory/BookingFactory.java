// src/main/java/com/example/Booking/factory/BookingFactory.java
package com.example.Booking.factory;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.FlightTicket;

import java.util.List;
import java.util.UUID;

public class BookingFactory {

    /**
     * Create a new Booking with default status= PENDING
     */
    public static Booking create(UUID userId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setStatus(BookingStatus.PENDING);
        return booking;
    }

    /**
     * (If you still need your old two-arg version, keep it here too.)
     */
    public static Booking create(UUID userId, List<FlightTicket> tickets) {
        Booking booking = create(userId);
        booking.setTickets(tickets);
        return booking;
    }
}

