package com.example.Booking.commads;

import com.example.Booking.Events.RabbitConfig;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class BookingCommandHandler {

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @RabbitListener(
            bindings = @QueueBinding(
                    value    = @Queue(value = RabbitConfig.COMMAND_QUEUE, durable = "true"),
                    exchange = @Exchange(value = RabbitConfig.EXCHANGE, type = ExchangeTypes.DIRECT),
                    key      = RabbitConfig.ROUTING_COMMAND
            ),
            containerFactory = "rabbitListenerContainerFactory"  // ✅ Ensures no requeue on error
    )
    public void handle(Command cmd) {
        try {
            beanFactory.autowireBeanProperties(
                    cmd,
                    AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE,
                    false
            );
            cmd.execute();
        } catch (Exception e) {
            System.err.println("❌ Command execution failed: " + e.getMessage());
            // Optional: log error or send to DLQ
        }
    }
}
