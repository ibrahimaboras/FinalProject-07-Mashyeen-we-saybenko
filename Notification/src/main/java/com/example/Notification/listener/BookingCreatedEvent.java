package com.example.Notification.listener;


import lombok.Data;
import java.util.List;

@Data
public class BookingCreatedEvent {
    private String userId;
    private List<FlightTicketDto> tickets;

    public String getUserId() {
        return userId;
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

