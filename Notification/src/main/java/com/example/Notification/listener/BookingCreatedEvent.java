package com.example.Notification.listener;


import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class BookingCreatedEvent {
    private String userId;
    private UUID bookingId;
    private List<FlightTicketDto> tickets;

    public String getUserId() {
        return userId;
    }

    public UUID getBookingId() {
        return bookingId;
    }

    public void setBookingId(UUID bookingId) {
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

