package com.example.Booking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.Booking.dto.PriceDTO;

import java.util.UUID;

@FeignClient(name = "flight-service")
public interface FlightServiceClient {
    @GetMapping("/prices/{priceId}")
    PriceDTO getFlightInfo(@PathVariable Long priceId);
}