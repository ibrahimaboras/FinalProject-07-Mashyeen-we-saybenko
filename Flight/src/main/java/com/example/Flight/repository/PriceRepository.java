package com.example.Flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Price;
import com.example.Flight.model.Seat;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByFlight(Flight flight);
    List<Price> findBySeat(Seat seat);
    Price findByFlightAndSeat(Flight flight, Seat seat);
}
