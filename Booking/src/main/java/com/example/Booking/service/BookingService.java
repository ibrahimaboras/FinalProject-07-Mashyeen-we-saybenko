package com.example.Booking.service;

import com.example.Booking.clients.FlightServiceClient;
import com.example.Booking.clients.UserServiceClient;
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
    private final UserServiceClient userServiceClient;
    private final FlightServiceClient flightServiceClient;

    public BookingService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo,
                          UserServiceClient userServiceClient,
                          FlightServiceClient flightServiceClient) {
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.userServiceClient = userServiceClient;
        this.flightServiceClient = flightServiceClient;
    }

    @Transactional
    public Booking createBooking(CreateBookingCommand cmd) {
        // 1. Validate user exists
        if (!userServiceClient.userExists(cmd.getUserId())) {
            throw new IllegalArgumentException("User does not exist");
        }

        // 2. Validate all tickets
        for (CreateBookingCommand.InitialTicket ticket : cmd.getTickets()) {
            // Check seat availability for each flight
            if (!flightServiceClient.isSeatAvailable(
                    ticket.getFlightId(),  // Use flightId from each ticket
                    1                     // Each ticket represents 1 seat
            )) {
                throw new IllegalStateException(
                        "Seat not available for flight: " + ticket.getFlightId()
                );
            }
        }

        // 3. Create and save booking
        Booking booking = BookingFactory.createBooking(cmd);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepo.save(booking);
    }

    public Booking getBooking(UUID id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));
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
        Booking booking = getBooking(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Booking already cancelled");
        }

        // Process refund
        BigDecimal totalPaid = booking.getPayments().stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED)
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal refundAmount = totalPaid.multiply(BigDecimal.valueOf(0.2));

        Payment refund = new Payment(
                UUID.randomUUID(),
                booking,
                refundAmount.negate(),
                "USD",
                PaymentStatus.REFUNDED,
                LocalDateTime.now()
        );

        paymentRepo.save(refund);
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepo.save(booking);
    }
}