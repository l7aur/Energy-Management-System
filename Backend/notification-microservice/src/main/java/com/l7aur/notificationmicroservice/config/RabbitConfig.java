package com.l7aur.notificationmicroservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${topic.exchange.name}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${notification.queue.name}")
    private String NOTIFICATION_QUEUE_NAME;

    @Value("${notification.routing.key}")
    private String NOTIFICATION_ROUTING_KEY;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue notificationMicroserviceQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME, true);
    }

    @Bean
    public Binding notificationMicroserviceBinding(TopicExchange topicExchange, Queue notificationMicroserviceQueue) {
        return BindingBuilder
                .bind(notificationMicroserviceQueue)
                .to(topicExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }
}
