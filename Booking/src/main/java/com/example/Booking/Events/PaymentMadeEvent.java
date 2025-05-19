package com.example.Booking.Events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMadeEvent {
    private String bookingId;
    private String paymentId;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime paidAt;
}