package com.example.Booking.commads;

import com.example.Booking.Events.BookingCreatedEvent;
import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.dto.FlightTicketDto;
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

        // 1) persist
        Booking saved = bookingService.createBooking(this);

        // 2) map tickets → DTOs for JSON
        List<FlightTicketDto> dtoTickets = tickets.stream()
                .map(it -> new FlightTicketDto(
                        it.getFullName(),
                        it.getNationality(),
                        it.getPassportNumber(),
                        it.getGender(),
                        it.getDateOfBirth().toString(),
                        it.getFlightId().toString(),
                        it.getSeatId().toString(),
                        it.getSeatClass().name()
                ))
                .toList();

        // 3) build your event payload
        BookingCreatedEvent event =
                new BookingCreatedEvent(
                        userId.toString(),
                        saved.getBookingId(),
                        dtoTickets
                );

        // 4) publish
        rabbit.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_CREATED,
                event
        );

        System.out.println("→ Sent booking.created → " + event);
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class InitialTicket {
        private String fullName;
        private String nationality;
        private String passportNumber;
        private String gender;
        private LocalDate dateOfBirth;
        private Long flightId;
        private Long seatId;
        private Long priceId;
        private SeatClass seatClass;
    }
}
