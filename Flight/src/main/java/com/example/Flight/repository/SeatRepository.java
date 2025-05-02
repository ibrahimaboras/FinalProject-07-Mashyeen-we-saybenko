package com.example.Flight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Seat;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByFlight(Flight flight);
    List<Seat> findByFlightAndIsAvailable(Flight flight, Boolean isAvailable);
    List<Seat> findByFlightAndClassType(Flight flight, String classType);
    Seat findBySeatNumberAndFlight(String seatNumber, Flight flight);
}