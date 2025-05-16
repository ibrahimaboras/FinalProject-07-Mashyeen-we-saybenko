package com.example.Booking.factory;


import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.model.FlightTicket;

public class FlightTicketFactory {
    public static FlightTicket createFlightTicket(CreateBookingCommand.InitialTicket t) {
        FlightTicket ticket = new FlightTicket();
        ticket.setFullName(t.getFullName());
        ticket.setNationality(t.getNationality());
        ticket.setPassportNumber(t.getPassportNumber());
        ticket.setGender(t.getGender());
        ticket.setDateOfBirth(t.getDateOfBirth());
        ticket.setSeatClass(t.getSeatClass());
        ticket.setFlightId(t.getFlightId());
        ticket.setSeatId(t.getSeatId());
        return ticket;
    }
}