package com.example.Booking.repository;

import com.example.Booking.model.Payment;
import com.example.Booking.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByBooking_BookingId(UUID bookingId);  // Find all payments for a booking
    boolean existsByPaymentIdAndStatus(UUID paymentId, PaymentStatus status);  // For validation
}