package com.example.Booking.commads;

import com.example.Booking.model.SeatClass;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingCommand implements Command , Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private List<InitialTicket> tickets;



    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class InitialTicket {
        private String fullName;
        private String nationality;
        private String passportNumber;
        private String gender;
        private LocalDate dateOfBirth;
        private UUID flightId;
        private UUID seatId;
        private SeatClass seatClass;
    }
}
