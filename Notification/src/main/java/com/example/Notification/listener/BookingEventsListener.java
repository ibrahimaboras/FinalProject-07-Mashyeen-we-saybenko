package com.example.Notification.listener;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookingEventsListener {



    @RabbitListener(queues = RabbitConfig.CREATED_QUEUE)
    public void onBookingCreated(BookingCreatedEvent event) {
        System.out.println("New booking is done check the ticket and pay:");
        System.out.println("   userId: "   + event.getUserId());
        System.out.println("   BookingID: "   + event.getBookingId());
        System.out.println("   tickets: "  + event.getTickets().size());

    }

    @RabbitListener(queues = RabbitConfig.CANCELLED_QUEUE)
    public void onBookingCancelled(CancelBookingEvent e) {
        System.out.println(" Booking cancelled:");
        System.out.println("   bookingId=" + e.getBookingId());
        System.out.println("   userId="    + e.getUserId());

    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void onPaymentMade(PaymentMadeEvent e) {
        System.out.println(" Payment Done:");
        System.out.println("   bookingId=" + e.getBookingId());
        System.out.println("   paymentId=" + e.getPaymentId());
        System.out.println("   amount="    + e.getAmount() + " " + e.getCurrency());

    }

}
