package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static com.example.Booking.Events.RabbitConfig.BOOKING_EXCHANGE;
import static com.example.Booking.Events.RabbitConfig.BOOKING_ROUTING_KEY;


@Component
public class CommandGateway {

    private final RabbitTemplate rabbit;

    public CommandGateway(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    /** Publish a CreateBookingCommand as JSON */
    public void send(CreateBookingCommand cmd) {
        rabbit.convertAndSend(BOOKING_EXCHANGE, BOOKING_ROUTING_KEY, cmd);
    }

    public void send(CancelBookingCommand cmd) {
        rabbit.convertAndSend(BOOKING_EXCHANGE, BOOKING_ROUTING_KEY, cmd);
    }
    }

