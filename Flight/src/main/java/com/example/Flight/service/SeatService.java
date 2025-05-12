package com.example.Flight.service;

// import com.example.Flight.exception.SeatNotFoundException;
import com.example.Flight.model.Seat;
import com.example.Flight.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;

    @Autowired
    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    public Seat getSeatById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
    }

    public List<Seat> getSeatsByFlight(Long flightId) {
        return seatRepository.findByFlightId(flightId);
    }

    public List<Seat> getAvailableSeatsByFlight(Long flightId) {
        return seatRepository.findByFlightIdAndIsAvailable(flightId, true);
    }

    public List<Seat> getSeatsByFlightAndClass(Long flightId, String classType) {
        return seatRepository.findByFlightIdAndClassType(flightId, classType);
    }

    public Seat createSeat(Seat seat) {
        // Check if seat with same number already exists on this flight
        Seat existingSeat = seatRepository.findBySeatNumberAndFlightId(
                seat.getSeatNumber(),
                seat.getFlight().getFlightId());
        
        return seatRepository.save(seat);
    }

    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));

        // Don't allow changing flight or seat number
        seat.setIsAvailable(seatDetails.getIsAvailable());
        seat.setClassType(seatDetails.getClassType());
        
        return seatRepository.save(seat);
    }

    public Seat updateSeatAvailability(Long id, Boolean isAvailable) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
        
        seat.setIsAvailable(isAvailable);
        return seatRepository.save(seat);
    }

    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));
        seatRepository.delete(seat);
    }
}