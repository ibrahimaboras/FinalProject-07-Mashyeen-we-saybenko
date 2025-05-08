package com.example.Booking.Events;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RabbitMQEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMQEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishPaymentCompletedEvent(UUID paymentId) {
        rabbitTemplate.convertAndSend(
                "payment.events.exchange",
                "payment.completed",
                new PaymentCompletedEvent(paymentId)
        );
    }

    public void publishPaymentRefundedEvent(UUID paymentId) {
        rabbitTemplate.convertAndSend(
                "payment.events.exchange",
                "payment.refunded",
                new PaymentRefundedEvent(paymentId)
        );
    }
}