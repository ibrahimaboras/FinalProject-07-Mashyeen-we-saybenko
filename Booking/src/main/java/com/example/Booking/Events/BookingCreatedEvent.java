package com.example.Booking.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.example.Booking.dto.FlightTicketDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {
    private String userId;
    private List<FlightTicketDto> tickets;
}