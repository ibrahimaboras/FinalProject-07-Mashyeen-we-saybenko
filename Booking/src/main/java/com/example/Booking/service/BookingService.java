package com.example.Booking.service;

import com.example.Booking.Exceptions.BookingNotFoundException;
import com.example.Booking.dto.BookingRequest;
import com.example.Booking.dto.BookingResponse;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.Events.RabbitMQEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;
    private final FlightTicketService flightTicketService;
    private final PaymentService paymentService;
    private final RabbitMQEventPublisher eventPublisher;

    public BookingService(BookingRepository bookingRepository,
                          FlightTicketService flightTicketService,
                          PaymentService paymentService,
                          RabbitMQEventPublisher eventPublisher) {
        this.bookingRepository = bookingRepository;
        this.flightTicketService = flightTicketService;
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // 1. Create and save the booking
        Booking booking = new Booking(); // Changed from builder()
        booking.setUserId(request.getUserId());
        booking = bookingRepository.save(booking);

        // 2. Add flight tickets
        request.getTickets().forEach(ticketRequest ->
                flightTicketService.createTicket(ticketRequest, booking.getBookingId()) // Changed to pass bookingId
        );

        // 3. Initiate payment
        paymentService.initiatePayment(booking.getBookingId(), request.getPaymentDetails());

        // 4. Publish event
        eventPublisher.publishEvent( // Unified event publishing method
                "booking.created",
                new BookingCreatedEvent(booking.getBookingId(), booking.getUserId())
        );

        return BookingResponse.from(booking);
    }

    public Booking getBooking(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));
    }

    @Transactional
    public void cancelBooking(UUID bookingId) {
        Booking booking = getBooking(bookingId);
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);

        // Refund payment
        eventPublisher.publishEvent(
                "booking.cancelled",
                new BookingCancelledEvent(bookingId)
        );
    }
}