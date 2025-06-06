package com.example.Flight.repository;

import com.example.Flight.model.Flight;
import com.example.Flight.model.Price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    // Custom query to find flights by origin and destination
    List<Flight> findByOriginAndDestination(String origin, String destination);

    // Custom query to find flights by origin, destination, and departure time range
    @Query("SELECT f FROM Flight f WHERE f.origin = :origin AND f.destination = :destination " +
           "AND f.departureTime >= :startTime AND f.departureTime <= :endTime")
    List<Flight> findByOriginAndDestinationAndDepartureTimeBetween(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    // Custom query to find flights by status
    List<Flight> findByStatus(String status);

    // Custom query to find flights with available seats greater than specified
    List<Flight> findByAvailableSeatsGreaterThan(int seats);

    // Custom query to find flights by aircraft model (via the Aircraft relationship)
    @Query("SELECT f FROM Flight f JOIN f.aircraft a WHERE a.model = :model")
    List<Flight> findByAircraftModel(@Param("model") String model);

    // Custom query to find flights by class type
    List<Flight> findByClassType(String classType);

    // Custom query to count available flights between two locations
    @Query("SELECT COUNT(f) FROM Flight f WHERE f.origin = :origin AND f.destination = :destination")
    long countFlightsByRoute(@Param("origin") String origin, @Param("destination") String destination);

    @Query("SELECT p FROM Price p WHERE p.flight.id = :id AND p.seat.isAvailable = true ORDER BY p.price")
    List<Price> findAvailablePricesByFlight(@Param("id") Long id);

    @Query("SELECT p FROM Price p WHERE p.flight.id = :id AND p.seat.classType = :classType")
    List<Price> findByidAndSeatClassType(@Param("id") Long id, @Param("classType") String classType);

    @Query("SELECT f FROM Flight f WHERE f.departureTime > :departureTime")
    List<Flight> findFlightsDepartingAfter(@Param("departureTime") LocalDateTime departureTime);
}