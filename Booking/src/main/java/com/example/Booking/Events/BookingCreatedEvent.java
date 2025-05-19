package com.example.Booking.Events;

import com.example.Booking.Events.FlightTicketDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private String userId;
    private List<FlightTicketDto> tickets;
}