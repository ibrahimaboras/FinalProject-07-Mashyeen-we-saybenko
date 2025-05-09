package com.example.Booking.repository;

import com.example.Booking.model.Booking;
import com.example.Booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    /** All bookings for a given user */
    List<Booking> findByUserId(UUID userId);

    /** All bookings for a given user in a particular status */
    List<Booking> findByUserIdAndStatus(UUID userId, BookingStatus status);

    /** All bookings with a given status (e.g. for batch processing or reporting) */
    List<Booking> findByStatus(BookingStatus status);

    /** Fetch a specific booking for a user, if it exists */
    Optional<Booking> findByBookingIdAndUserId(UUID bookingId, UUID userId);

    /** Check ownership of a booking without loading the full entity */
    boolean existsByBookingIdAndUserId(UUID bookingId, UUID userId);
}
