package com.example.Booking.model;// com/example/booking/model/Booking.java

import com.example.Booking.model.FlightTicket;
import com.example.Booking.model.Payment;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID bookingId;

    @Column(nullable = false)
    private UUID userId;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FlightTicket> tickets;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Booking() {
        this.tickets = new ArrayList<>();
        this.payments = new ArrayList<>();
        this.status = BookingStatus.PENDING;
    }

    public Booking(UUID bookingId,
                   UUID userId,
                   List<FlightTicket> tickets,
                   List<Payment> payments,
                   BookingStatus status,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.tickets = (tickets != null ? tickets : new ArrayList<>());
        this.payments = (payments != null ? payments : new ArrayList<>());
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void addTicket(FlightTicket t) {
        tickets.add(t);
        t.setBooking(this);
    }

    public void addPayment(Payment p) {
        p.setBooking(this);
        this.payments.add(p);
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
        if (status == null) status = BookingStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
