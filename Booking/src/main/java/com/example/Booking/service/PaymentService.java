package com.example.Booking.service;// com/example/booking/service/PaymentService.java

import com.example.Booking.commads.MakePaymentCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import com.example.Booking.repository.BookingRepository;
import com.example.Booking.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.example.Booking.model.BookingStatus.CONFIRMED;

@Service
public class PaymentService {
    private final BookingRepository bookingRepo;
    private final PaymentRepository paymentRepo;

    public PaymentService(BookingRepository bookingRepo,
                          PaymentRepository paymentRepo) {
        this.bookingRepo = bookingRepo;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public Payment makePayment(MakePaymentCommand cmd) {
        Booking b = bookingRepo.findById(cmd.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment p = new Payment(
                UUID.randomUUID(),
                b,
                cmd.getAmount(),
                cmd.getCurrency(),
                PaymentStatus.COMPLETED,
                LocalDateTime.now()
        );

        Payment saved = paymentRepo.save(p);
        bookingRepo.updateStatus(b.getBookingId(), BookingStatus.CONFIRMED);  // add this custom query
        return saved;

    }
}
