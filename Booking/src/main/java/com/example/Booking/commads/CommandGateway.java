package com.example.Booking.commads;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommandGateway {
    private final RabbitTemplate rabbitTemplate;
    public CommandGateway(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void send(Command cmd) {

        rabbitTemplate.convertAndSend("booking.exchange", "booking.commands", cmd);

    }
}