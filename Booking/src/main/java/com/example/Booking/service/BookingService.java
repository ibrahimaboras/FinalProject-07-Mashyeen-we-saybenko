package com.example.Booking.service;

import com.example.Booking.clients.FlightServiceClient;
import com.example.Booking.clients.UserServiceClient;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.dto.FlightDTO;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final CommandGateway commandGateway;
    private final UserServiceClient userServiceClient;
    private final FlightServiceClient flightServiceClient; // Added Feign client

    public BookingService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo,
                          CommandGateway commandGateway,
                          UserServiceClient userServiceClient,
                          FlightServiceClient flightServiceClient) { // Added parameter
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.commandGateway = commandGateway;
        this.userServiceClient = userServiceClient;
        this.flightServiceClient = flightServiceClient;
    }

    @Transactional
    public Booking createBooking(CreateBookingCommand cmd) {
        try {
            if (cmd.getTickets() == null || cmd.getTickets().isEmpty()) {
                throw new IllegalArgumentException("Booking must include at least one ticket.");
            }

            // For simplicity, we take the first ticket to get flight info
            var firstTicket = cmd.getTickets().get(0);
            UUID flightId = firstTicket.getFlightId();
            UUID seatId = firstTicket.getSeatId();

            FlightDTO flight = flightServiceClient.getFlightById(flightId);

            if (!"ACTIVE".equalsIgnoreCase(flight.getStatus())) {
                throw new IllegalStateException("Flight is not active.");
            }

            if (flight.getAvailableSeats() <= 0) {
                throw new IllegalStateException("No seats available.");
            }

            // 2. Reserve seat
            flightServiceClient.reserveSeat(flightId, seatId);

            // 3. Create & save booking
            Booking booking = BookingFactory.createBooking(cmd);
            booking.setStatus(BookingStatus.PENDING);
            Booking saved = bookingRepo.save(booking);

            // 4. Publish event
            commandGateway.send("booking.exchange", "booking.created", cmd);

            // 5. Notify user
            notifyUserService(saved.getBookingId(), saved.getUserId(), "created");

            return saved;

        } catch (Exception e) {
            System.err.println("Failed to create booking: " + e.getMessage());
            throw new RuntimeException("Booking creation failed.", e);
        }
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
}