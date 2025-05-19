package com.example.Notification.listener;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // exchange shared with Booking service
    public static final String EXCHANGE               = "booking.exchange";

    // queues for each event
    public static final String CREATED_QUEUE          = "notification.booking.created";
    public static final String CANCELLED_QUEUE        = "notification.booking.cancelled";
    public static final String PAYMENT_QUEUE          = "notification.booking.payment";

    // routing keys from Booking service
    public static final String CREATED_ROUTING_KEY    = "booking.created";
    public static final String CANCELLED_ROUTING_KEY  = "booking.cancelled";
    public static final String PAYMENT_ROUTING_KEY    = "booking.payment_made";

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue createdQueue() {
        return new Queue(CREATED_QUEUE, true);
    }

    @Bean
    public Queue cancelledQueue() {
        return new Queue(CANCELLED_QUEUE, true);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE, true);
    }

    @Bean
    public Binding bindingCreated(Queue createdQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(createdQueue)
                .to(bookingExchange)
                .with(CREATED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingCancelled(Queue cancelledQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(cancelledQueue)
                .to(bookingExchange)
                .with(CANCELLED_ROUTING_KEY);
    }

    @Bean
    public Binding bindingPayment(Queue paymentQueue, DirectExchange bookingExchange) {
        return BindingBuilder
                .bind(paymentQueue)
                .to(bookingExchange)
                .with(PAYMENT_ROUTING_KEY);
    }

    // ————————————
    // Custom JSON converter that ignores upstream __TypeId__ headers
    // and infers from your listener method signature.
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        // ignore broker’s __TypeId__, use method’s parameter type
        typeMapper.setTypePrecedence(DefaultJackson2JavaTypeMapper.TypePrecedence.INFERRED);
        // trust only your DTO package
        typeMapper.setTrustedPackages("com.example.Notification.dto");
        converter.setJavaTypeMapper(typeMapper);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory cf,
            Jackson2JsonMessageConverter converter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(cf);
        factory.setMessageConverter(converter);
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
