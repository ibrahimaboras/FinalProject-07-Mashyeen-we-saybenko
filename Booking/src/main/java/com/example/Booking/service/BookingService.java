package com.example.Booking.service;

import com.example.Booking.clients.FlightServiceClient;
import com.example.Booking.clients.UserServiceClient;
import com.example.Booking.Events.BookingNotificationProducer;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.dto.FlightTicketDto;
import com.example.Booking.dto.PriceDTO;
import com.example.Booking.factory.BookingFactory;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.Payment;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.PaymentRepository;
import com.netflix.discovery.converters.Auto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.Booking.model.PaymentStatus.REFUNDED;

@Service
public class BookingService {
    private final BookingNotificationProducer producer;
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;
    private final CommandGateway commandGateway;

    @Autowired
    private FlightServiceClient flightServiceClient;

    public BookingService(BookingNotificationProducer producer, BookingRepository bookingRepo,
                          PaymentRepository paymentRepo, CommandGateway commandGateway) {
        this.producer = producer;
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
        this.commandGateway = commandGateway;
    }

    @Transactional
    public Booking createBooking(CreateBookingCommand cmd) {
        try {
//            Booking b = BookingFactory.createBooking(cmd);
//            b.setStatus(BookingStatus.PENDING);
//            return bookingRepo.save(b);

            Class<?> factoryClass = Class.forName("com.example.Booking.factory.BookingFactory");
            Method createMethod = factoryClass.getMethod("createBooking", CreateBookingCommand.class);
            Booking b = (Booking) createMethod.invoke(null, cmd);
            b.setStatus(BookingStatus.PENDING);
            return bookingRepo.save(b);
        } catch (Exception e) {
            throw new RuntimeException("Reflection error", e);
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
        Booking b = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        if (b.getStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Already cancelled");
        }

        // 1) If there is a payment, reduce its amount by 20%
        Optional<Payment> paymentOpt = paymentRepo.findByBooking_BookingId(bookingId);
        paymentOpt.ifPresent(p -> {
            BigDecimal original = p.getAmount();
            BigDecimal reduced = original.multiply(new BigDecimal("0.8"));  // keep 80%
            p.setAmount(reduced);
            p.setStatus(REFUNDED);

            paymentRepo.save(p);
        });

        // 2) Cancel the booking
        b.setStatus(BookingStatus.CANCELLED);
        return bookingRepo.save(b);
    }

    // New private helper method to handle user notifications
    // private void notifyUserService(UUID bookingId, Long userId, String action) {
    //     try {
    //         String notification = String.format(
    //                 "Booking %s - ID: %s, User: %s",
    //                 action,
    //                 bookingId,
    //                 userId
    //         );
    //         userServiceClient.sendBookingNotification(userId, notification);
    //     } catch (Exception e) {
    //         // Log error but don't interrupt main flow
    //         System.err.println("Failed to notify user service: " + e.getMessage());
    //     }
    // }

    public PriceDTO getFlightInfoByPriceId(Long priceId) {
        return flightServiceClient.getFlightInfo(priceId);
    }
}