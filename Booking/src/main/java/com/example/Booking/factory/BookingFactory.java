package com.example.Booking.factory;


import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.model.Booking;

public class BookingFactory {
    public static Booking createBooking(CreateBookingCommand cmd) {
        Booking booking = new Booking();
        booking.setUserId(cmd.getUserId());
        cmd.getTickets()
                .forEach(t -> booking.addTicket(FlightTicketFactory.createFlightTicket(t)));
        return booking;
    }
}
