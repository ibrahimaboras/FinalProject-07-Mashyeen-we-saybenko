package com.example.Notification.listener;

import com.example.Notification.model.Notification;
import com.example.Notification.model.NotificationType;
import com.example.Notification.service.NotificationService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Component
@Service
public class BookingEventsListener {

    private final NotificationService notificationService;

    public BookingEventsListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @RabbitListener(queues = RabbitConfig.CREATED_QUEUE)
    public void onBookingCreated(BookingCreatedEvent event) {
        System.out.println("New booking is done check the ticket and pay:");
        System.out.println("   userId: "   + event.getUserId());
        System.out.println("   BookingID: "   + event.getBookingId());
        System.out.println("   tickets: "  + event.getTickets().size());

        String message = "Booking created for user " + event.getUserId() +
                " with " + event.getTickets().size() + " ticket(s).";

        Notification notification = new Notification();
        notification.setUserId(Long.parseLong(event.getUserId())); // Convert from String
        notification.setBookingId(Long.parseLong(event.getBookingId()));
        notification.setType(NotificationType.EMAIL); // or SMS, as you prefer
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationService.sendAndStoreNotification(notification);

    }

    @RabbitListener(queues = RabbitConfig.CANCELLED_QUEUE)
    public void onBookingCancelled(CancelBookingEvent event) {
        System.out.println(" Booking cancelled:");
        System.out.println("   bookingId=" + event.getBookingId());
        System.out.println("   userId="    + event.getUserId());

        String message = "Booking cancelled for user " + event.getUserId();

        Notification notification = new Notification();
        notification.setUserId(Long.parseLong(event.getUserId()));
        notification.setBookingId(Long.parseLong(event.getBookingId()));
        notification.setType(NotificationType.EMAIL);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationService.sendAndStoreNotification(notification);

    }

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void onPaymentMade(PaymentMadeEvent e) {
        System.out.println(" Payment Done:");
        System.out.println("   bookingId=" + e.getBookingId());
        System.out.println("   paymentId=" + e.getPaymentId());
        System.out.println("   amount="    + e.getAmount() + " " + e.getCurrency());

        String message = "Payment of " + e.getAmount() + " " + e.getCurrency() +
                " was made for booking " + e.getBookingId();

        Notification notification = new Notification();
        notification.setUserId(null); // No userId provided in event
        notification.setBookingId(Long.parseLong(e.getBookingId()));
        notification.setType(NotificationType.EMAIL);
        notification.setMessage(message);
        notification.setTimestamp(LocalDateTime.now());

        notificationService.sendAndStoreNotification(notification);

    }

}
