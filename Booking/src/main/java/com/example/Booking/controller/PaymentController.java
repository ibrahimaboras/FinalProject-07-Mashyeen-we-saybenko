package com.example.Booking.controller;

import com.example.Booking.commads.MakePaymentCommand;
import com.example.Booking.model.Payment;
import com.example.Booking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/bookings/{bookingId}/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public Payment pay(
            @PathVariable UUID bookingId,
            @RequestBody MakePaymentCommand cmdBody
    ) {
        var cmd = new MakePaymentCommand(
                bookingId,
                cmdBody.getAmount(),
                cmdBody.getCurrency()
        );
        return paymentService.pay(cmd);
    }
}
