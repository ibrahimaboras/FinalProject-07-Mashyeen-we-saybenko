package com.example.Booking.Events;

import java.util.UUID;

public class PaymentRefundedEvent {
    private final UUID paymentId;

    public PaymentRefundedEvent(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public UUID getPaymentId() { return paymentId; }
}