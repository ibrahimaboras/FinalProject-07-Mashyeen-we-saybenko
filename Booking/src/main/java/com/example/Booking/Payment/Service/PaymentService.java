package com.example.Booking.Payment.Service;


import com.example.Booking.Events.RabbitMQEventPublisher;
import com.example.Booking.Payment.Exceptions.PaymentFailedException;
import com.example.Booking.Payment.dto.PaymentRequest;
import com.example.Booking.Payment.dto.PaymentResponse;
import com.example.Booking.Payment.model.Payment;
import com.example.Booking.Payment.model.PaymentStatus;
import com.example.Booking.Payment.repository.PaymentRepository;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final RabbitMQEventPublisher eventPublisher;

    @Transactional
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Booking booking = (Booking) bookingRepository.findByUserId(request.getBookingId());

        Payment payment = Payment.builder()
                .booking(booking)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .build();

        payment = paymentRepository.save(payment);

        // Simulate payment processing (replace with real payment gateway call)
        boolean isPaymentSuccessful = processPayment(payment);

        if (isPaymentSuccessful) {
            payment.setStatus(PaymentStatus.COMPLETED);
            booking.setStatus(BookingStatus.CONFIRMED);
            eventPublisher.publishPaymentCompletedEvent(payment.getPaymentId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            throw new PaymentFailedException("Payment failed for booking: " + booking.getBookingId());
        }

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        return PaymentResponse.fromPayment(payment);
    }

    private boolean processPayment(Payment payment) {
        // Mock logic - in reality, call Stripe/PayPal/etc.
        return !payment.getPaymentMethod().equals("FAIL_CARD");
    }

    @Transactional
    public void refundPayment(UUID paymentId) {
        Payment payment = (Payment) paymentRepository.findByBooking_BookingId(paymentId);

        payment.setStatus(PaymentStatus.REFUNDED);
        paymentRepository.save(payment);

        eventPublisher.publishPaymentRefundedEvent(paymentId);
    }
}