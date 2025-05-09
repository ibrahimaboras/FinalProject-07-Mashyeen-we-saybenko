package com.example.Booking.service;


import com.example.Booking.Events.RabbitMQEventPublisher;
import com.example.Booking.Exceptions.PaymentFailedException;
import com.example.Booking.dto.PaymentDetails;
import com.example.Booking.dto.PaymentRequest;
import com.example.Booking.dto.PaymentResponse;
import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import com.example.Booking.repository.PaymentRepository;
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
    public Payment initiatePayment(Booking booking, PaymentRequest  request) {



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

            paymentRepository.save(payment);
            bookingRepository.save(booking);

            eventPublisher.publishPaymentCompletedEvent(payment.getPaymentId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentFailedException("Payment failed for booking: " + booking.getBookingId());
        }



        return payment;
    }
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException(
                        "Booking not found: " + request.getBookingId()
                ));
        Payment payment = initiatePayment(booking, request);
        return PaymentResponse.fromPayment(payment);
    }





    private boolean processPayment(Payment payment) {
        // Mock logic - in reality, call Stripe/PayPal/etc.
        return !payment.getPaymentMethod().equals("FAIL_CARD");
    }

    @Transactional
    public PaymentResponse refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        payment.setStatus(PaymentStatus.REFUNDED);
        payment = paymentRepository.save(payment);
        eventPublisher.publishPaymentRefundedEvent(paymentId);
        return PaymentResponse.fromPayment(payment);
    }
}