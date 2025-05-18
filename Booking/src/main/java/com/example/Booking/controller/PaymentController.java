package com.example.Booking.controller;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.commads.CommandGateway;
import com.example.Booking.commads.MakePaymentCommand;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import com.example.Booking.model.Payment;
import com.example.Booking.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final CommandGateway gateway;


    public PaymentController(CommandGateway gateway , PaymentService paymentService) {
        this.gateway = gateway;
        this.paymentService = paymentService;
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

    @GetMapping
    public List<Payment> list(
    ) {

        return paymentService.getallpayment();
    }
}
