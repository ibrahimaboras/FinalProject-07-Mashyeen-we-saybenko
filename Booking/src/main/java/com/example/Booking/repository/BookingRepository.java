package com.example.Booking.repository;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(UUID userId);
    List<Booking> findByStatus(BookingStatus status);
}