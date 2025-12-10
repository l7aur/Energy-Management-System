package com.l7aur.monitoringmicroservice.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class EnergyManagementTopicConfig {
    @Value("${topic.exchange.name}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${monitoring.microservice.queue}")
    private String MONITORING_QUEUE_NAME;

    @Value("${monitoring.routing.key}")
    private String MONITORING_ROUTING_KEY;

    @Value("${notification.microservice.queue}")
    private String NOTIFICATION_QUEUE_NAME;

    @Value("${notification.routing.key}")
    private String NOTIFICATION_ROUTING_KEY;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue monitoringMicroserviceQueue() {
        return new Queue(MONITORING_QUEUE_NAME, true);
    }

    @Bean
    public Binding monitoringBinding(TopicExchange topicExchange, Queue monitoringMicroserviceQueue) {
        return BindingBuilder
                .bind(monitoringMicroserviceQueue)
                .to(topicExchange)
                .with(MONITORING_ROUTING_KEY);
    }

    @Bean
    public Queue notificationMicroserviceQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME, true);
    }

    @Bean
    public Binding notificationBinding(TopicExchange topicExchange, Queue notificationMicroserviceQueue) {
        return BindingBuilder
                .bind(notificationMicroserviceQueue)
                .to(topicExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }
}
