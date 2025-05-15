package com.example.Booking.service;// com/example/booking/service/BookingService.java



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

    public BookingService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo) {
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public Booking createBooking(CreateBookingCommand cmd) {
        Booking b = BookingFactory.createBooking(cmd);
        b.setStatus(BookingStatus.PENDING);
        return bookingRepo.save(b);
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
        // 20% refund
        BigDecimal totalPaid = b.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal refundAmt = totalPaid.multiply(BigDecimal.valueOf(0.2));
        String currency = b.getPayments().stream()
                .findFirst().map(Payment::getCurrency).orElse("USD");

        Payment refund = new Payment(
                UUID.randomUUID(),
                b,
                refundAmt.negate(),
                currency,
                PaymentStatus.REFUNDED,
                LocalDateTime.now()
        );
        b.addPayment(refund);
        paymentRepo.save(refund);

        return bookingRepo.save(b);
    }
}
