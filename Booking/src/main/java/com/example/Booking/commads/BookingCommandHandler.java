package com.example.Booking.commads;

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

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "booking.commands", durable = "true"),
                    exchange = @Exchange(value = "booking.exchange", type = ExchangeTypes.DIRECT),
                    key = "booking.commands"
            )
    )
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