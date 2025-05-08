package com.example.Booking.repository;

import com.example.Booking.model.FlightTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlightTicketRepository extends JpaRepository<FlightTicket, UUID> {

    List<FlightTicket> findByBookingBookingId(UUID bookingId);
}
