package com.example.Booking.Events;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "booking.exchange";
    public static final String CREATED_ROUTING_KEY  = "booking.created";
    public static final String CANCELLED_ROUTING_KEY = "booking.cancelled";
    public static final String CREATED_QUEUE  = "booking.created.queue";
    public static final String CANCELLED_QUEUE = "booking.cancelled.queue";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue createdQueue() {
        return QueueBuilder.durable(CREATED_QUEUE).build();
    }

    @Bean
    public Queue cancelledQueue() {
        return QueueBuilder.durable(CANCELLED_QUEUE).build();
    }

    @Bean
    public Binding createdBinding(Queue createdQueue, TopicExchange exchange) {
        return BindingBuilder.bind(createdQueue).to(exchange).with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding cancelledBinding(Queue cancelledQueue, TopicExchange exchange) {
        return BindingBuilder.bind(cancelledQueue).to(exchange).with(CANCELLED_ROUTING_KEY);
    }
}
