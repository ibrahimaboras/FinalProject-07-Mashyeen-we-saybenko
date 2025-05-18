package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
import com.example.Booking.service.BookingService;
import com.example.Booking.service.PaymentService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class BookingCommandHandler {
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public BookingCommandHandler(BookingService bookingService,
                                 PaymentService paymentService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    // BookingCommandHandler.java
    @RabbitListener(bindings = @QueueBinding(
            value    = @Queue(value = RabbitConfig.BOOKING_ROUTING_KEY, durable = "true"),
            exchange = @Exchange(value = RabbitConfig.BOOKING_EXCHANGE, type = ExchangeTypes.DIRECT),
            key      = RabbitConfig.BOOKING_ROUTING_KEY
    ))
    public void handle(Command cmd) {
        if (cmd instanceof CreateBookingCommand c) {
            bookingService.createBooking(c);
        } else if (cmd instanceof MakePaymentCommand m) {
            paymentService.makePayment(m);
        } else if (cmd instanceof CancelBookingCommand x) {
            bookingService.cancelBooking(x.getBookingId());
        }
    }
}