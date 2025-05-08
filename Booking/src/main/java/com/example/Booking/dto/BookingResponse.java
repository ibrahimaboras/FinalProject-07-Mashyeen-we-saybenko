package com.example.Booking.dto;

import com.example.Booking.model.Booking;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;

@Data
public class BookingResponse {
    private UUID bookingId;
    private LocalDateTime createdAt;
    private String status;

    public static BookingResponse from(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setCreatedAt(booking.getCreatedAt());
        response.setStatus(booking.getStatus().toString());
        return response;
    }
}