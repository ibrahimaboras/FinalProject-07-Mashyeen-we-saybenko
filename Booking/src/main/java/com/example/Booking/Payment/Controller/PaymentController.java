package com.example.Booking.Payment.Controller;


import com.example.Booking.dto.PaymentRequest;
import com.example.Booking.dto.PaymentResponse;
import com.example.Booking.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse initiatePayment(@Valid @RequestBody PaymentRequest request) {
        return paymentService.initiatePayment(request);
    }

    @PostMapping("/{paymentId}/refund")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void refundPayment(@PathVariable UUID paymentId) {
        paymentService.refundPayment(paymentId);
    }
}