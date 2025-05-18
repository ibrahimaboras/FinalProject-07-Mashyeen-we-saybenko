package com.example.Booking.controller;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.MakePaymentCommand;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final CommandGateway gateway;

    public PaymentController(CommandGateway gateway) {
        this.gateway = gateway;
    }

    @PostMapping
    public ResponseEntity<Void> pay(@RequestBody MakePaymentCommand cmd) {
        gateway.send(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_COMMAND,
                cmd
        );
        return ResponseEntity.accepted().build();
    }
}
