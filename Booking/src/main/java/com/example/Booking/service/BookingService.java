package com.example.Booking.service;// com/example/booking/service/BookingService.java



import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final CommandGateway commandGateway;

    public BookingService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo, CommandGateway commandGateway) {
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.commandGateway  = commandGateway;
    }

    @Transactional
    public Booking createBooking(CreateBookingCommand cmd) {
        // 1) create & save
        Booking b = BookingFactory.createBooking(cmd);
        b.setStatus(BookingStatus.PENDING);
        Booking saved = bookingRepo.save(b);

        // 2) publish to RabbitMQ
        commandGateway.send("booking.exchange",
                "booking.created",
                cmd);

        return saved;
    }
    public Booking getBooking(UUID id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> getBookingsByUser(UUID userId) {
        return bookingRepo.findByUserId(userId);
    }

    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepo.findByStatus(status);
    }

    @Transactional
    public Booking cancelBooking(UUID bookingId) {
        Booking b = getBooking(bookingId);
        if (b.getStatus() == BookingStatus.CANCELLED)
            throw new RuntimeException("Already cancelled");

        b.setStatus(BookingStatus.CANCELLED);
        // ... refund logic ...
        Booking updated = bookingRepo.save(b);

        // publish cancellation
        CancelBookingCommand cancelCmd = new CancelBookingCommand(bookingId);
        commandGateway.send("booking.exchange",
                "booking.cancelled",
                cancelCmd);

        return updated;
    }

}
