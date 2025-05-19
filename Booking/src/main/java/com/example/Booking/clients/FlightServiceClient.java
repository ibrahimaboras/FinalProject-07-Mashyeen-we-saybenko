package com.example.Booking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "flight-service", url = "http://localhost:8080/api/flights")
public interface FlightServiceClient {
    @GetMapping("/flights/{flightId}")
    Object getFlight(@PathVariable String flightId);
}