package com.example.Notification.Events;

import com.example.Booking.commads.CreateBookingCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiver {

    @RabbitListener(queues = RabbitConfig.NOTIF_QUEUE)
    public void onBookingCreated(CreateBookingCommand cmd) {
        System.out.println("Received booking for userId=" + cmd.getUserId());

        var tickets = cmd.getTickets();
        if (tickets == null || tickets.isEmpty()) {
            System.out.println("→ No tickets in this booking");
            return;
        }

        for (CreateBookingCommand.InitialTicket ticket : tickets) {
            System.out.println("→ Ticket for passenger " + ticket.getFullName() + ":");
            System.out.println("     • Nationality:   " + ticket.getNationality());
            System.out.println("     • Passport No:   " + ticket.getPassportNumber());
            System.out.println("     • Gender:        " + ticket.getGender());
            System.out.println("     • Date of Birth: " + ticket.getDateOfBirth());
            System.out.println("     • Flight ID:     " + ticket.getFlightId());
            System.out.println("     • Seat ID:       " + ticket.getSeatId());
            System.out.println("     • Seat Class:    " + ticket.getSeatClass());
        }
    }
}
