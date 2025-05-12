package com.example.Booking.model;

import com.example.Booking.model.Booking;
import com.example.Booking.model.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@Entity
@Table(name = "payments")
public class Payment {

    public Payment(UUID paymentId, Booking booking, BigDecimal amount, String currency, PaymentStatus status, LocalDateTime paidAt) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.paidAt = paidAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    private BigDecimal amount;
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private LocalDateTime paidAt;

    public Payment() {

    }
}
