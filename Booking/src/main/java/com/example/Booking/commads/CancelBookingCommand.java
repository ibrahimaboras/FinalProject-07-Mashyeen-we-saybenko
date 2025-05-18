package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
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
    @Serial private static final long serialVersionUID = 1L;

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
        if (bookingService == null || rabbit == null) {
            throw new IllegalStateException("Dependencies not injected");
        }

        bookingService.cancelBooking(bookingId);
        rabbit.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_CANCELLED,
                bookingId
        );

        System.out.println("✅ Booking cancelled, event sent → " + bookingId);
    }
}

