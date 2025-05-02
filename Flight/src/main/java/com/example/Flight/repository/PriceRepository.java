package com.example.Flight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Price;
import com.example.Flight.model.Seat;

import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {
    List<Price> findByFlightId(Long flight);
    List<Price> findBySeat(Seat seat);
    Price findByFlightAndSeat(Flight flight, Seat seat);

    // Find minimum price for each flight
    @Query("SELECT p.flight, MIN(p.price) as minPrice FROM Price p GROUP BY p.flight ORDER BY minPrice")
    List<Object[]> findFlightsWithMinPrice();
    
    // Find prices for available seats
    @Query("SELECT p FROM Price p WHERE p.seat.isAvailable = true AND p.flight = :flight ORDER BY p.price")
    List<Price> findAvailablePricesByFlight(Flight flight);

    @Query("SELECT p FROM Price p WHERE p.flight.id = :flightId AND p.seat.classType = :classType ORDER BY p.price")
    List<Price> findByFlightIdAndSeatClassType(Long flightId, String classType);

}
