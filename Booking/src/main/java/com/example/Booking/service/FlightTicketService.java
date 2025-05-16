package com.example.Booking.service;

import com.example.Booking.model.FlightTicket;
import com.example.Booking.repository.FlightTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlightTicketService {
    private final FlightTicketRepository ticketRepo;

    public FlightTicketService(FlightTicketRepository ticketRepo) {
        this.ticketRepo = ticketRepo;
    }

    public FlightTicket getTicket(UUID id) {
        return ticketRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public List<FlightTicket> getTicketsByBooking(UUID bookingId) {
        return ticketRepo.findByBookingBookingId(bookingId);
    }
}
