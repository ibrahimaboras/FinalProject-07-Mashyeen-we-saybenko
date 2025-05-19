package com.example.Booking.commads;

import com.example.Booking.Events.CancelBookingEvent;
import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.model.Booking;
import com.example.Booking.service.BookingService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
public class CancelBookingCommand implements Command, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UUID bookingId;

    public CancelBookingCommand(UUID bookingId) {
        this.bookingId = bookingId;
    }

    @JsonIgnore
    private transient BookingService bookingService;

    @JsonIgnore
    private transient RabbitTemplate rabbit;

    @Override
    public void execute() {
        // 1) perform cancellation
        Booking cancelled = bookingService.cancelBooking(bookingId);

        // 2) build event payload
        CancelBookingEvent event = new CancelBookingEvent(
                bookingId.toString(),
                cancelled.getUserId().toString()
        );

        // 3) publish
        rabbit.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_CANCELLED,
                event
        );
        System.out.println("→ Sent booking.cancelled → " + event);
    }
}
