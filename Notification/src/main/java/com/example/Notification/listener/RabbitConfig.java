package com.example.Notification.listener;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "booking.exchange";
    public static final String QUEUE    = "notification.queue";
    public static final String ROUTING_CREATED = "booking.created";
    public static final String ROUTING_CANCELLED = "booking.cancelled";

    @Bean
    public TopicExchange bookingExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding bindingCreated(Queue notificationQueue, TopicExchange bookingExchange) {
        return BindingBuilder.bind(notificationQueue).to(bookingExchange).with(ROUTING_CREATED);
    }

    @Bean
    public Binding bindingCancelled(Queue notificationQueue, TopicExchange bookingExchange) {
        return BindingBuilder.bind(notificationQueue).to(bookingExchange).with(ROUTING_CANCELLED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(converter);
        return f;
    }
}
