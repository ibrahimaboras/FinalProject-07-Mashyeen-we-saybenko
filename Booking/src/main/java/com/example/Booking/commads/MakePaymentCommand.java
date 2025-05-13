package com.example.Booking.commads;// MakePaymentCommand.java


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/** Make / record a payment for a booking. */
@Getter @RequiredArgsConstructor
public class MakePaymentCommand {
    private final UUID bookingId;
    private final BigDecimal amount;
    private final String currency;
}
