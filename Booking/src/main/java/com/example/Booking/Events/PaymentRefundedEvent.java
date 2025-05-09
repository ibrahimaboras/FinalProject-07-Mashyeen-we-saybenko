package com.example.Booking.Events;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Event emitted when a payment is refunded.
 * Includes refund metadata for auditing.
 */
@Getter
@ToString
@EqualsAndHashCode
public final class PaymentRefundedEvent implements Serializable {
    private final UUID paymentId;

    public PaymentRefundedEvent(UUID paymentId) {
        this.paymentId = paymentId;

    }
}