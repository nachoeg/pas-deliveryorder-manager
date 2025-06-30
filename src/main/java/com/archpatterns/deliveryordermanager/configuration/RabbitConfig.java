package com.archpatterns.deliveryordermanager.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${app.rabbitmq.make-order-queue}")
    private String makeOrderQueueName;

    @Bean
    public Queue makeOrderQueue() {
        return new Queue(makeOrderQueueName, true);
    }
}
