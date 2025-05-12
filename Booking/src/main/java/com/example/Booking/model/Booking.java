package com.example.Booking.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID userId;  // Reference to UserService (eventual consistency)

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightTicket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BookingStatus status;  // Enum: PENDING, CONFIRMED, CANCELLED

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;




    public Booking() {
        this.tickets = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.status   = BookingStatus.PENDING;
    }


    public Booking(
            UUID bookingId,
            UUID userId,
            List<FlightTicket> tickets,
            List<Payment> payments,
            BookingStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.bookingId = bookingId;
        this.userId    = userId;
        this.tickets   = (tickets   != null ? tickets   : new ArrayList<>());
        this.payments  = (payments  != null ? payments  : new ArrayList<>());
        this.status    = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        status    = BookingStatus.PENDING;  // ensure default
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addTicket(FlightTicket ticket) {
        tickets.add(ticket);
        ticket.setBooking(this);
    }

    public void removeTicket(FlightTicket ticket) {
        tickets.remove(ticket);
        ticket.setBooking(null);
    }
}
