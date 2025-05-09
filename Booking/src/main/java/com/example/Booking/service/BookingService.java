// src/main/java/com/example/Booking/service/BookingService.java
package com.example.Booking.service;

import com.example.Booking.Events.BookingCreatedEvent;
import com.example.Booking.Events.BookingCancelledEvent;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.Payment;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.Events.RabbitMQEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository       bookingRepository;
    private final FlightTicketService    flightTicketService;
    private final PaymentService         paymentService;
    private final RabbitMQEventPublisher eventPublisher;

    /**
     * Create a new booking, persist its tickets & initial payment,
     * then publish a BookingCreatedEvent.
     */
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1) instantiate & save booking
        Booking booking = BookingFactory.create(request.getUserId());
        booking = bookingRepository.save(booking);

        // 2) create & attach tickets
        for (var tr : request.getTickets()) {
            flightTicketService.createTicket(tr, booking);
        }

        // 3) initiate payment & attach
        Payment payment = paymentService.initiatePayment(booking, request.getPaymentDetails());
        booking.getPayments().add(payment);

        // 4) publish domain event
        eventPublisher.publishBookingCreatedEvent(
                booking.getBookingId(),
                booking.getUserId()
        );

        return BookingResponse.from(booking);
    }

    /** Fetch a booking by ID */
    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));
        return BookingResponse.from(booking);
    }

    /**
     * Cancel a booking (if not already cancelled),
     * persist the change, then emit BookingCancelledEvent.
     */
    @Transactional
    public void cancelBooking(UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (booking.getStatus() != BookingStatus.CANCELLED) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingRepository.save(booking);

            eventPublisher.publishBookingCancelledEvent(bookingId);
        }
    }
}
