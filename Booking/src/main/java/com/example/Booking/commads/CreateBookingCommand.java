package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.model.Booking;
import com.example.Booking.model.SeatClass;
import com.example.Booking.service.BookingService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateBookingCommand implements Command, Serializable {
    @Serial private static final long serialVersionUID = 1L;

    private Long userId;
    private List<InitialTicket> tickets;

    // these won’t be serialized into JSON
    @JsonIgnore

    private transient BookingService bookingService;

    @JsonIgnore
    private transient RabbitTemplate rabbit;

    @Override
    public void execute() {

        Booking b = bookingService.createBooking(this);

        rabbit.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_CREATED,
                b.getBookingId()
        );
        System.out.println("→ Event: " + RabbitConfig.ROUTING_CREATED + " → " + b.getBookingId());
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class InitialTicket {
        private String fullName;
        private String nationality;
        private String passportNumber;
        private String gender;
        private LocalDate dateOfBirth;
        private UUID flightId;
        private UUID seatId;
        private SeatClass seatClass;
    }
}
