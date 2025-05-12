package com.example.Booking.service;

import com.example.Booking.Events.RabbitMQEventPublisher;
import com.example.Booking.commads.MakePaymentCommand;
import com.example.Booking.model.Booking;
import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import com.example.Booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final BookingRepository bookingRepo;
    private final RabbitMQEventPublisher events;

    public Payment pay(MakePaymentCommand cmd) {
        Booking b = bookingRepo.findById(cmd.getBookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        Payment p = new Payment();
        p.setBooking(b);
        p.setAmount(cmd.getAmount());
        p.setCurrency(cmd.getCurrency());
        p.setPaidAt(LocalDateTime.now());
        p.setStatus(PaymentStatus.COMPLETED);
        b.addPayment(p);

        bookingRepo.save(b);
        events.publishPaymentCompletedEvent(p.getPaymentId());
        return p;
    }
}
