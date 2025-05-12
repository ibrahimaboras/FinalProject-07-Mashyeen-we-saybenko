package com.example.Booking.service;

import com.example.Booking.Events.RabbitMQEventPublisher;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepo;
    private final RabbitMQEventPublisher events;

    public Booking create(CreateBookingCommand cmd) {
        Booking b = BookingFactory.from(cmd);
        Booking saved = bookingRepo.save(b);
        events.publishBookingCreatedEvent(saved.getBookingId(), saved.getUserId());
        return saved;
    }

    public Booking cancel(CancelBookingCommand cmd) {
        Booking b = bookingRepo.findById(cmd.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        if (b.getStatus() != BookingStatus.CANCELLED) {
            b.setStatus(BookingStatus.CANCELLED);
            bookingRepo.save(b);
            events.publishBookingCancelledEvent(b.getBookingId());
        }
        return b;
    }

    public Booking getById(java.util.UUID id) {
        return bookingRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Booking not found"
                        ));
    }
}
