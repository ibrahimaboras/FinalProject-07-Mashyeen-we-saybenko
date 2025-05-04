package com.example.Booking.Payment.Events;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentEventConsumer {
    @RabbitListener(queues = "payment.refunded.queue")
    public void handleRefund(PaymentRefundedEvent event) {
        // Trigger refund logic
    }
}