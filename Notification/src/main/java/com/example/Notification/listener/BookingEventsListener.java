package com.example.Notification.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventsListener {



    @RabbitListener(queues = RabbitConfig.NOTIF_QUEUE)
    public void onBookingCreated(BookingCreatedEvent event) {
        System.out.println("New booking is done check the ticket and pay:");
        System.out.println("   userId: "   + event.getUserId());
        System.out.println("   tickets: "  + event.getTickets().size());

    }
}
