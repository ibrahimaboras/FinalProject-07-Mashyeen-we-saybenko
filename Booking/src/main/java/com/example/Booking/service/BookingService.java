package com.example.Booking.service;

import com.example.Booking.clients.FlightServiceClient;
import com.example.Booking.clients.UserServiceClient;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.dto.PriceDTO;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.PaymentRepository;
import com.netflix.discovery.converters.Auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final CommandGateway commandGateway;
    private final UserServiceClient userServiceClient; // Added Feign client

    @Autowired
    private FlightServiceClient flightServiceClient;

    public BookingService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo,
                          CommandGateway commandGateway,
                          UserServiceClient userServiceClient) { // Added parameter
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.commandGateway = commandGateway;
        this.userServiceClient = userServiceClient;
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

        // 3) Notify user service (added)
        notifyUserService(saved.getBookingId(), saved.getUserId(), "created");

        return saved;
    }

    public Booking getBooking(UUID id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
    }

    public List<Booking> getAllBookings() {
        return bookingRepo.findAll();
    }

    public List<Booking> getBookingsByUser(Long userId) {
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

        // Notify user service (added)
        notifyUserService(updated.getBookingId(), updated.getUserId(), "cancelled");

        return updated;
    }

    // New private helper method to handle user notifications
    private void notifyUserService(UUID bookingId, Long userId, String action) {
        try {
            String notification = String.format(
                    "Booking %s - ID: %s, User: %s",
                    action,
                    bookingId,
                    userId
            );
            userServiceClient.sendBookingNotification(userId, notification);
        } catch (Exception e) {
            // Log error but don't interrupt main flow
            System.err.println("Failed to notify user service: " + e.getMessage());
        }
    }

    public PriceDTO getFlightInfoByPriceId(Long priceId) {
        return flightServiceClient.getFlightInfo(priceId);
    }
}