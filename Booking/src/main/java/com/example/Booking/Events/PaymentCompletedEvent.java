package com.example.Booking.Events;


import java.util.UUID;

public class PaymentCompletedEvent {
    private final UUID paymentId;

    public PaymentCompletedEvent(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getPaymentId() { return paymentId; }
}