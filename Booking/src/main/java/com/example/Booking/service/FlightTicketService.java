package com.example.Booking.service;

import com.example.Booking.commads.AddFlightTicketCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.model.FlightTicket;
import com.example.Booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class FlightTicketService {
    private final BookingRepository bookingRepo;

    public FlightTicket addTicket(AddFlightTicketCommand cmd) {
        Booking b = bookingRepo.findById(cmd.getBookingId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Booking not found"
                        ));

        FlightTicket t = new FlightTicket();
        t.setFullName(cmd.getFullName());
        t.setNationality(cmd.getNationality());
        t.setPassportNumber(cmd.getPassportNumber());
        t.setGender(cmd.getGender());
        t.setDateOfBirth(cmd.getDateOfBirth());
        t.setFlightId(cmd.getFlightId());
        t.setSeatId(cmd.getSeatId());
        b.addTicket(t);

        // cascade = ALL on tickets
        bookingRepo.save(b);
        return t;
    }
}
