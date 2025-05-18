package com.example.Booking.Events;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Exchange and routing key constants
    public static final String EXCHANGE           = "booking.exchange";
    public static final String QUEUE              = "booking.commands";
    public static final String ROUTING_CREATED    = "booking.created";
    public static final String ROUTING_CANCELLED  = "booking.cancelled";
    public static final String ROUTING_PAYMENT    = "booking.payment_made";

    // Declare a DirectExchange (matches existing exchange type in RabbitMQ)
    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    // Declare a durable queue
    @Bean
    public Queue bookingQueue() {
        return new Queue(QUEUE, true);
    }

    // Bindings for each routing key
    @Bean
    public Binding bindCreated(Queue bookingQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(ROUTING_CREATED);
    }

    @Bean
    public Binding bindCancelled(Queue bookingQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(ROUTING_CANCELLED);
    }

    @Bean
    public Binding bindPayment(Queue bookingQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(bookingQueue)
                .to(bookingExchange)
                .with(ROUTING_PAYMENT);
    }

    // JSON message converter
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // RabbitTemplate with JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}
