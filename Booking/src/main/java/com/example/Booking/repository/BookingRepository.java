package com.example.Booking.repository;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    List<Booking> findByUserId(Long userId);
    List<Booking> findByStatus(BookingStatus status);
    @Modifying
    //@Transactional
    @Query("UPDATE Booking b SET b.status = :status WHERE b.bookingId = :id")
    void updateStatus(@Param("id") UUID id, @Param("status") BookingStatus status);
}