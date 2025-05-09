// src/main/java/com/example/Booking/dto/PaymentDto.java
package com.example.Booking.dto;

import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentDto {
    private UUID paymentId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PaymentDto from(Payment p) {
        PaymentDto dto = new PaymentDto();
        dto.paymentId = p.getPaymentId();
        dto.amount    = p.getAmount();
        dto.currency  = p.getCurrency();
        dto.status    = p.getStatus();
        dto.createdAt = p.getCreatedAt();
        dto.updatedAt = p.getUpdatedAt();
        return dto;
    }

    // getters
    public UUID getPaymentId()   { return paymentId; }
    public BigDecimal getAmount(){ return amount; }
    public String getCurrency()  { return currency; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
