package com.example.Booking.commads;

import com.example.Booking.commads.Command;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandGateway {
    private final RabbitTemplate rabbit;
    private final String exchange;
    private final String routingKey;

    public CommandGateway(RabbitTemplate rabbit,
                          @Value("${rabbitmq.exchange:booking.exchange}") String exchange,
                          @Value("${rabbitmq.routing-key:booking.commands}") String routingKey) {
        this.rabbit = rabbit;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void send(Command cmd) {
        rabbit.convertAndSend(exchange, routingKey, cmd);
    }
}