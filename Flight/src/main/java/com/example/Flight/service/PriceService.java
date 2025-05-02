package com.example.Flight.service;

import com.example.Flight.model.Price;
import com.example.Flight.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    public Price getPriceById(Long id) {
        Optional<Price> price = priceRepository.findById(id);
        if (price.isPresent()) {
            return price.get();
        } else {
            throw new RuntimeException("Price not found with id: " + id);
        }
    }

    public List<Price> getPricesByFlight(Long flightId) {
        return priceRepository.findByFlightId(flightId);
    }

    public List<Price> getPricesBySeat(Long seatId) {
        return priceRepository.findBySeatId(seatId);
    }

    public Price getPriceByFlightAndSeat(Long flightId, Long seatId) {
        return priceRepository.findByFlightIdAndSeatId(flightId, seatId);
    }

    public Price createPrice(Price price) {
        // Check if price already exists for this flight and seat combination
        Price existingPrice = priceRepository.findByFlightIdAndSeatId(
                price.getFlight().getFlightId(), 
                price.getSeat().getSeatId());
        
        return priceRepository.save(price);
    }

    public Price updatePrice(Long id, Price priceDetails) {
        Price price = priceRepository.findById(id).get();

        // Only update the price value, not the flight or seat relationships
        price.setPrice(priceDetails.getPrice());
        
        return priceRepository.save(price);
    }

    public void deletePrice(Long id) {
        Price price = priceRepository.findById(id).get();
        priceRepository.delete(price);
    }
}