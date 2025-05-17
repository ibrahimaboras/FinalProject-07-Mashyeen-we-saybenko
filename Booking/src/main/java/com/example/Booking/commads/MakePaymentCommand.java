package com.example.Booking.commads;// MakePaymentCommand.java


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class MakePaymentCommand implements Command , Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID bookingId;
    private final BigDecimal amount;
    private final String currency;
}
