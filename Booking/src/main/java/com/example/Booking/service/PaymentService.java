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
import java.util.List;
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
    public void makePayment(MakePaymentCommand cmd) {
        UUID bookingId = cmd.getBookingId();

        // 1) Load the booking
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + bookingId));

        // 2) Try to load an existing payment, else create one
        Payment payment = paymentRepo
                .findByBooking_BookingId(bookingId)
                .orElseGet(() -> {
                    Payment p = new Payment();
                    p.setBooking(booking);
                    return p;
                });


        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            return;
        }


        payment.setAmount(cmd.getAmount());
        payment.setCurrency(cmd.getCurrency());
        payment.setPaidAt(LocalDateTime.now());
        payment.setStatus(PaymentStatus.COMPLETED);

        payment = paymentRepo.save(payment);


        booking.setStatus(BookingStatus.COMPLETED);
        booking.getPayments().add(payment);      // if you have a OneToMany

        bookingRepo.save(booking);
    }

    @Transactional
    public List<Payment> getallpayment() {
        return paymentRepo.findAll();
    }

}

