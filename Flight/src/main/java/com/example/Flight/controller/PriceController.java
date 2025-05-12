package com.example.Flight.controller;

import com.example.Flight.model.Price;
import com.example.Flight.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<Price> getAllPrices() {
        return priceService.getAllPrices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Price> getPriceById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(priceService.getPriceById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/flight/{flightId}")
    public List<Price> getPricesByFlight(@PathVariable Long flightId) {
        return priceService.getPricesByFlight(flightId);
    }

    @GetMapping("/seat/{seatId}")
    public List<Price> getPricesBySeat(@PathVariable Long seatId) {
        return priceService.getPricesBySeat(seatId);
    }

    @GetMapping("/flight/{flightId}/seat/{seatId}")
    public ResponseEntity<Price> getPriceByFlightAndSeat(
            @PathVariable Long flightId,
            @PathVariable Long seatId) {
        Price price = priceService.getPriceByFlightAndSeat(flightId, seatId);
        if (price != null) {
            return ResponseEntity.ok(price);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createPrice(@RequestBody Price price) {
        try {
            return ResponseEntity.ok(priceService.createPrice(price));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Price> updatePrice(@PathVariable Long id, @RequestBody Price priceDetails) {
        try {
            return ResponseEntity.ok(priceService.updatePrice(id, priceDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        try {
            priceService.deletePrice(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}