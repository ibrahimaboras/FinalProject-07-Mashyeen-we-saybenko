package com.example.Booking.dto;

import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponse {
    private UUID paymentId;
    private UUID bookingId;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    public static PaymentResponse fromPayment(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.setPaymentId(payment.getPaymentId());
        response.setBookingId(payment.getBooking().getBookingId());
        response.setAmount(payment.getAmount());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());
        return response;
    }
}