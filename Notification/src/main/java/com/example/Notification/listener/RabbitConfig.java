package com.example.Notification.listener;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE         = "booking.exchange";
    public static final String NOTIF_QUEUE      = "notification.booking.queue";
    public static final String ROUTING_KEY      = "booking.created";

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIF_QUEUE, true);
    }

    @Bean
    public Binding binding(Queue notificationQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(notificationQueue)
                .to(bookingExchange)
                .with(ROUTING_KEY);
    }

    // ⬇️ override the default converter
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // tell it to ignore the incoming __TypeId__ header
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.INFERRED);
        // trust only your Notification DTO package
        typeMapper.setTrustedPackages("com.example.Notification.dto");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory f = new SimpleRabbitListenerContainerFactory();
        f.setConnectionFactory(cf);
        f.setMessageConverter(converter);
        // single‐threaded, no requeue on error
        f.setConcurrentConsumers(1);
        f.setMaxConcurrentConsumers(1);
        f.setDefaultRequeueRejected(false);
        return f;
    }
}
