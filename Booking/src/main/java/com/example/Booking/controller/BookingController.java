package com.example.Booking.controller;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.clients.UserServiceClient;
import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.CreateBookingCommand;
import com.example.Booking.dto.PriceDTO;
import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
public class BookingController {
    private final CommandGateway gateway;
    private final BookingService service;
    private final UserServiceClient userServiceClient;

    public BookingController(CommandGateway gateway,
                             BookingService service, UserServiceClient userServiceClient) {
        this.gateway = gateway;
        this.service = service;
        this.userServiceClient = userServiceClient;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateBookingCommand cmd) {
        gateway.send(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_COMMAND,
                cmd
        );
        return ResponseEntity.accepted().build();
    }

    // @GetMapping("/{id}")
    // public Booking get(@PathVariable UUID id) {
    //     return service.getBooking(id);
    // }

    @GetMapping
    public List<Booking> list(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) BookingStatus status
    ) {
        if (userId != null)   return service.getBookingsByUser(userId);
        if (status != null)   return service.getBookingsByStatus(status);
        return service.getAllBookings();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable UUID id) {
        CancelBookingCommand cancelCmd = new CancelBookingCommand(id);
        gateway.send(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_COMMAND,
                cancelCmd
        );
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId) {
        List<Booking> bookings = service.getBookingsByUser(userId);
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{priceID}/getFlightInfo")
    public ResponseEntity<PriceDTO> getFlightInfo(@PathVariable Long priceID) {
        return ResponseEntity.ok(service.getFlightInfoByPriceId(priceID));
    }
}
