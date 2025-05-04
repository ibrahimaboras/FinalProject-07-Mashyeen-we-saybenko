package com.example.Booking.service;

import com.example.Booking.model.Booking;
import com.example.Booking.model.FlightTicket;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.FlightTicketRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlightTicketService {

    private final FlightTicketRepository flightTicketRepository;
    private final BookingRepository bookingRepository;

    public FlightTicketService(FlightTicketRepository flightTicketRepository, BookingRepository bookingRepository) {
        this.flightTicketRepository = flightTicketRepository;
        this.bookingRepository = bookingRepository;
    }

    public FlightTicket createFlightTicket(UUID bookingId, FlightTicket ticket) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        ticket.setBooking(booking);
        return flightTicketRepository.save(ticket);
    }

    public List<FlightTicket> getTicketsByBooking(UUID bookingId) {
        return flightTicketRepository.findByBookingBookingId(bookingId);
    }

    public void deleteTicket(UUID ticketId) {
        flightTicketRepository.deleteById(ticketId);
    }
}
