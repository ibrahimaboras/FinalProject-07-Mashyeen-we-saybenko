// src/main/java/com/example/Booking/dto/BookingResponse.java
package com.example.Booking.dto;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingResponse {
    private UUID bookingId;
    private UUID userId;
    private BookingStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FlightTicketDto> tickets;
    private List<PaymentDto> payments;

    public static BookingResponse from(Booking b) {
        BookingResponse r = new BookingResponse();
        r.bookingId = b.getBookingId();
        r.userId    = b.getUserId();
        r.status    = b.getStatus();
        r.createdAt = b.getCreatedAt();
        r.updatedAt = b.getUpdatedAt();
        r.tickets   = b.getTickets().stream()
                .map(FlightTicketDto::from)
                .collect(Collectors.toList());
        r.payments  = b.getPayments().stream()
                .map(PaymentDto::from)
                .collect(Collectors.toList());
        return r;
    }
    public UUID getBookingId() { return bookingId; }
    public UUID getUserId()    { return userId; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<FlightTicketDto> getTickets() { return tickets; }
    public List<PaymentDto> getPayments()    { return payments; }

}
