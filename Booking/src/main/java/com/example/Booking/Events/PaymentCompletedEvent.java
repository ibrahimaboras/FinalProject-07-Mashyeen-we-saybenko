package com.example.Booking.Events;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * Event emitted when a payment is successfully completed.
 * Immutable to ensure thread safety during message processing.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class PaymentCompletedEvent implements Serializable {
    private final UUID paymentId;

    public PaymentCompletedEvent(UUID paymentId) {
        this.paymentId = paymentId;
    }
}