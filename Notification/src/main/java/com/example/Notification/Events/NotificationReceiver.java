package com.example.Notification.Events;

import com.example.Booking.commads.CreateBookingCommand;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiver {

    @RabbitListener(
            queues = RabbitConfig.NOTIF_QUEUE,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void onBookingCreated(CreateBookingCommand cmd) {
        System.out.println("📣 New booking for userId=" + cmd.getUserId());
        cmd.getTickets().forEach(t ->
                System.out.println("   • ticket: flight=" + t.getFlightId()
                        + " seat=" + t.getSeatId()
                        + " class=" + t.getSeatClass())
        );
    }

}
