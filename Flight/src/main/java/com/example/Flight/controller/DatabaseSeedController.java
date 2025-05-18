package com.example.Flight.controller;

import com.example.Flight.model.Aircraft;
import com.example.Flight.model.Flight;
import com.example.Flight.model.Seat;
import com.example.Flight.model.Price;
import com.example.Flight.repository.AircraftRepository;
import com.example.Flight.repository.FlightRepository;
import com.example.Flight.repository.SeatRepository;
import com.example.Flight.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/flightSeed")
public class DatabaseSeedController {

    private final AircraftRepository aircraftRepository;
    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;
    private final PriceRepository priceRepository;

    @Autowired
    public DatabaseSeedController(
            AircraftRepository aircraftRepository,
            FlightRepository flightRepository,
            SeatRepository seatRepository,
            PriceRepository priceRepository) {
        this.aircraftRepository = aircraftRepository;
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
        this.priceRepository = priceRepository;
    }

    @PostMapping
    public ResponseEntity<String> seedDatabase() {
        try {
            // Clear existing data
            priceRepository.deleteAll();
            seatRepository.deleteAll();
            flightRepository.deleteAll();
            aircraftRepository.deleteAll();

            // Create Aircraft
            List<Aircraft> aircrafts = Arrays.asList(
                    new Aircraft("Boeing 737", 180, "Delta Airlines"),
                    new Aircraft("Airbus A320", 150, "American Airlines"),
                    new Aircraft("Boeing 787", 250, "United Airlines")
            );
            aircraftRepository.saveAll(aircrafts);

            // Create Flights
            List<Flight> flights = Arrays.asList(
                    new Flight.Builder()
                            .aircraft(aircrafts.get(0))
                            .origin("JFK")
                            .destination("LAX")
                            .departureTime(LocalDateTime.now().plusDays(1))
                            .arrivalTime(LocalDateTime.now().plusDays(1).plusHours(6))
                            .status("Scheduled")
                            .classType("Economy")
                            .availableSeats(180)
                            .gateInfo("Gate A12")
                            .build(),
                    new Flight.Builder()
                            .aircraft(aircrafts.get(1))
                            .origin("ORD")
                            .destination("MIA")
                            .departureTime(LocalDateTime.now().plusDays(2))
                            .arrivalTime(LocalDateTime.now().plusDays(2).plusHours(3))
                            .status("Scheduled")
                            .classType("Business")
                            .availableSeats(150)
                            .gateInfo("Gate B5")
                            .build(),
                    new Flight.Builder()
                            .aircraft(aircrafts.get(2))
                            .origin("SFO")
                            .destination("SEA")
                            .departureTime(LocalDateTime.now().plusDays(3))
                            .arrivalTime(LocalDateTime.now().plusDays(3).plusHours(2))
                            .status("Scheduled")
                            .classType("Economy")
                            .availableSeats(250)
                            .gateInfo("Gate C3")
                            .build()
            );
            flightRepository.saveAll(flights);

            // Create Seats and Prices
            for (Flight flight : flights) {
                int capacity = flight.getAircraft().getCapacity();
                int economySeats = (int) (capacity * 0.8); // 80% Economy
                int businessSeats = capacity - economySeats; // 20% Business

                // Economy Seats
                for (int i = 1; i <= economySeats; i++) {
                    String seatNumber = "E" + i;
                    Seat seat = new Seat(flight, seatNumber, true, "Economy");
                    seatRepository.save(seat);

                    // Price for Economy seat
                    Price price = new Price(flight, seat, 200.0 + (i % 5) * 10.0); // Varying prices
                    priceRepository.save(price);
                }

                // Business Seats
                for (int i = 1; i <= businessSeats; i++) {
                    String seatNumber = "B" + i;
                    Seat seat = new Seat(flight, seatNumber, true, "Business");
                    seatRepository.save(seat);

                    // Price for Business seat
                    Price price = new Price(flight, seat, 500.0 + (i % 3) * 50.0); // Varying prices
                    priceRepository.save(price);
                }
            }

            return ResponseEntity.ok("Database seeded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error seeding database: " + e.getMessage());
        }
    }
}