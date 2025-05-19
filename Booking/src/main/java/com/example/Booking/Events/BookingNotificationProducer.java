package com.example.Booking.Events;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.example.Booking.dto.FlightTicketDto;

import java.util.List;
import java.util.UUID;

@Service
public class BookingNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public BookingNotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookingCreated(UUID userId, UUID bookingId, List<FlightTicketDto> tickets) {
        BookingCreatedEvent event =
                new BookingCreatedEvent(userId.toString(), bookingId.toString() , tickets);

        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_CREATED,    // "booking.created"
                event
        );
        System.out.println("Sent booking.created → " + event);
    }
}
