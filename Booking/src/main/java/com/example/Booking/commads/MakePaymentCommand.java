package com.example.Booking.commads;

import com.example.Booking.Events.PaymentMadeEvent;
import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.model.Payment;
import com.example.Booking.service.PaymentService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @RequiredArgsConstructor
public class MakePaymentCommand implements Command, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID bookingId;
    private final BigDecimal amount;
    private final String currency;

    @JsonIgnore
    private transient PaymentService paymentService;

    @JsonIgnore
    private transient RabbitTemplate rabbit;


    @Override
    public void execute() {
        // 1) make payment
        Payment payment = paymentService.makePayment(this);

        // 2) build event payload
        PaymentMadeEvent event = new PaymentMadeEvent(
                bookingId.toString(),
                payment.getPaymentId().toString(),
                amount,
                currency,
                payment.getPaidAt()
        );

        // 3) publish *after commit*
        rabbit.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_PAYMENT,
                event
        );
        System.out.println("→ Sent booking.payment_made → " + event);
    }
}


