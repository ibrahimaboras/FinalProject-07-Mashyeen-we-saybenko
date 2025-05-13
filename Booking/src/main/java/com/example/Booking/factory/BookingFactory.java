package com.example.Booking.factory;


import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.model.FlightTicket;

public class BookingFactory {
    public static Booking from(CreateBookingCommand cmd) {
        Booking b = new Booking();
        b.setUserId(cmd.getUserId());
        for (var it : cmd.getTickets()) {
            FlightTicket t = new FlightTicket();
            t.setFullName(it.getFullName());
            t.setNationality(it.getNationality());
            t.setPassportNumber(it.getPassportNumber());
            t.setGender(it.getGender());
            t.setDateOfBirth(it.getDateOfBirth());
            t.setFlightId(it.getFlightId());
            t.setSeatId(it.getSeatId());
            b.addTicket(t);
        }
        return b;
    }
}
