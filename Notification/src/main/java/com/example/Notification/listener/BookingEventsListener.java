package com.example.Notification.listener;

import com.example.Booking.commads.CancelBookingCommand;
import com.example.Booking.commads.Command;
import com.example.Booking.commads.CreateBookingCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventsListener {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleBookingCommand(Command cmd) {
        if (cmd instanceof CreateBookingCommand create) {
            System.out.println("Received booking.created → booking ID: " );
        }
        else if (cmd instanceof CancelBookingCommand cancel) {
            System.out.println("Received booking.cancelled → booking ID: " + cancel.getBookingId());
        }
    }
}
