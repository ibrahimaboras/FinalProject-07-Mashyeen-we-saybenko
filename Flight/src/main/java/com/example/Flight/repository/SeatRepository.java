package com.example.Flight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlightId(Long flight);
    List<Seat> findByFlightIdAndIsAvailable(Long flight, Boolean isAvailable);
    List<Seat> findByFlightIdAndClassType(Long flight, String classType);
    Seat findBySeatNumberAndFlightId(String seatNumber, Long flight);
}