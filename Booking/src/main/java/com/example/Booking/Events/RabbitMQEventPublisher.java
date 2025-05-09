// src/main/java/com/example/Booking/Events/RabbitMQEventPublisher.java
package com.example.Booking.Events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String bookingExchange;
    private final String paymentExchange;

    public RabbitMQEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${booking.rabbitmq.exchange:booking.events.exchange}") String bookingExchange,
            @Value("${payment.rabbitmq.exchange:payment.events.exchange}") String paymentExchange
    ) {
        this.rabbitTemplate   = rabbitTemplate;
        this.bookingExchange  = bookingExchange;
        this.paymentExchange  = paymentExchange;
    }

    // ─── Booking Events ─────────────────────────────────────────────────────────

    /** Publish when a new booking is created */
    public void publishBookingCreatedEvent(UUID bookingId, UUID userId) {
        rabbitTemplate.convertAndSend(
                bookingExchange,
                "booking.created",
                new BookingCreatedEvent(bookingId, userId)
        );
    }

    /** Publish when an existing booking is cancelled */
    public void publishBookingCancelledEvent(UUID bookingId) {
        rabbitTemplate.convertAndSend(
                bookingExchange,
                "booking.cancelled",
                new BookingCancelledEvent(bookingId)
        );
    }

    // ─── Payment Events ─────────────────────────────────────────────────────────

    /** Publish when a payment is completed */
    public void publishPaymentCompletedEvent(UUID paymentId) {
        rabbitTemplate.convertAndSend(
                paymentExchange,
                "payment.completed",
                new PaymentCompletedEvent(paymentId)
        );
    }

    /** Publish when a payment is refunded */
    public void publishPaymentRefundedEvent(UUID paymentId) {
        rabbitTemplate.convertAndSend(
                paymentExchange,
                "payment.refunded",
                new PaymentRefundedEvent(paymentId)
        );
    }


}
