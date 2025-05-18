package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.service.PaymentService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @RequiredArgsConstructor
public class MakePaymentCommand implements Command, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private final UUID    bookingId;
    private final BigDecimal amount;
    private final String   currency;

    @JsonIgnore
    private transient PaymentService paymentService;

    @JsonIgnore
    private transient RabbitTemplate rabbit;


    @Override
    public void execute() {
        try {
            paymentService.makePayment(this);
            rabbit.convertAndSend(
                    RabbitConfig.EXCHANGE,
                    RabbitConfig.ROUTING_PAYMENT,
                    this.bookingId
            );
            System.out.println("→ Event: " + RabbitConfig.ROUTING_PAYMENT + " → " + this.bookingId);
        } catch (Exception e) {
            System.err.println("❌ Payment failed for booking " + bookingId + ": " + e.getMessage());
            // optional: send to dead-letter exchange, or persist to a failed message table
        }
    }
}
