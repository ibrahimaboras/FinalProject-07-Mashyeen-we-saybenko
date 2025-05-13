package com.example.Booking.controller;

import com.example.Booking.commads.AddFlightTicketCommand;
import com.example.Booking.model.FlightTicket;
import com.example.Booking.service.FlightTicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings/{bookingId}/tickets")
@RequiredArgsConstructor
public class FlightTicketController {
    private final FlightTicketService ticketService;

    @PostMapping
    public FlightTicket addTicket(
            @PathVariable UUID bookingId,
            @RequestBody AddFlightTicketCommand cmdBody
    ) {
        // ensure command has bookingId
        var cmd = new AddFlightTicketCommand(
                bookingId,
                cmdBody.getFullName(),
                cmdBody.getNationality(),
                cmdBody.getPassportNumber(),
                cmdBody.getGender(),
                cmdBody.getDateOfBirth(),
                cmdBody.getFlightId(),
                cmdBody.getSeatId()
        );
        return ticketService.addTicket(cmd);
    }
}
