package com.example.Booking.Events;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // 1) Exchange
    public static final String EXCHANGE = "booking.exchange";

    // 2) Queues
    public static final String COMMAND_QUEUE = "booking.commands";
    public static final String EVENT_QUEUE   = "booking.events";

    // 3) Routing keys
    public static final String ROUTING_COMMAND   = "booking.command";
    public static final String ROUTING_CREATED   = "booking.created";
    public static final String ROUTING_CANCELLED = "booking.cancelled";
    public static final String ROUTING_PAYMENT   = "booking.payment_made";

    // --- Exchange bean ---
    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    // --- Queues ---
    @Bean
    public Queue commandQueue() {
        return new Queue(COMMAND_QUEUE, true);
    }

    @Bean
    public Queue eventQueue() {
        return new Queue(EVENT_QUEUE, true);
    }

    // --- Bind commands to the command queue ---
    @Bean
    public Binding bindCommands(Queue commandQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(commandQueue)
                .to(bookingExchange)
                .with(ROUTING_COMMAND);
    }

    // --- Bind each event to the event queue ---
    @Bean
    public Binding bindCreatedEvent(Queue eventQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(eventQueue)
                .to(bookingExchange)
                .with(ROUTING_CREATED);
    }

    @Bean
    public Binding bindCancelledEvent(Queue eventQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(eventQueue)
                .to(bookingExchange)
                .with(ROUTING_CANCELLED);
    }

    @Bean
    public Binding bindPaymentEvent(Queue eventQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(eventQueue)
                .to(bookingExchange)
                .with(ROUTING_PAYMENT);
    }

    // --- JSON converter + RabbitTemplate ---
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setDefaultRequeueRejected(false);  // ✅ KEY LINE
        return factory;
    }
}
