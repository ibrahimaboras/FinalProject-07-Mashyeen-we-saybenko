package com.example.Booking.Events;// src/main/java/com/example/booking/events/RabbitMQEventPublisher.java

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String bookingExchange;
    private final String paymentExchange;

    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate,
                                  @Value("${booking.rabbitmq.exchange:booking.events}") String bookingExchange,
                                  @Value("${payment.rabbitmq.exchange:payment.events}") String paymentExchange) {
        this.rabbitTemplate   = rabbitTemplate;
        this.bookingExchange  = bookingExchange;
        this.paymentExchange  = paymentExchange;
    }

    /** Publish when a new booking is created */
    public void publishBookingCreatedEvent(UUID bookingId, UUID userId) {
        rabbitTemplate.convertAndSend(
                bookingExchange,
                "booking.created",
                new BookingCreatedEvent(bookingId, userId)
        );
    }

    /** Publish when a booking is cancelled */
    public void publishBookingCancelledEvent(UUID bookingId) {
        rabbitTemplate.convertAndSend(
                bookingExchange,
                "booking.cancelled",
                new BookingCancelledEvent(bookingId)
        );
    }

    /** Publish when a payment completes */
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
