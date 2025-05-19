package com.example.Notification.listener;


import lombok.Data;
import java.util.List;

@Data
public class BookingCreatedEvent {
    private String userId;
    private  String bookingId;
    private List<FlightTicketDto> tickets;

    public String getUserId() {
        return userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<FlightTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<FlightTicketDto> tickets) {
        this.tickets = tickets;
    }


}

