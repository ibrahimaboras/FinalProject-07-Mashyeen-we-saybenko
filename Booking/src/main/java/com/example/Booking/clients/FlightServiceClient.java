package com.example.Booking.clients;

import com.example.Booking.dto.FlightDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.UUID;

@FeignClient(name = "flight-service", url = "http://localhost:8081") // Replace with service name if using service discovery
public interface FlightServiceClient {

    @GetMapping("/api/flights/{flightId}")
    FlightDTO getFlightById(@PathVariable("flightId") UUID flightId);

    @PutMapping("/api/flights/reserve-seat/{flightId}/{seatId}")
    void reserveSeat(@PathVariable("flightId") UUID flightId, @PathVariable("seatId") UUID seatId);
}