package com.l7aur.devicemicroservice.config;

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
public class RabbitConfig {
    @Value("${topic.exchange.name}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${device.microservice.queue}")
    private String DEVICE_QUEUE_NAME;

    @Value("${device.routing.key}")
    private String DEVICE_ROUTING_KEY;

    @Value("${monitoring.microservice.queue}")
    private String MONITORING_QUEUE_NAME;

    @Value("${monitoring.routing.key}")
    private String MONITORING_ROUTING_KEY;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue deviceMicroserviceQueue() {
        return new Queue(DEVICE_QUEUE_NAME, true);
    }

    @Bean
    public Binding deviceQueueBinding(TopicExchange topicExchange, Queue deviceMicroserviceQueue) {
        return BindingBuilder
                .bind(deviceMicroserviceQueue)
                .to(topicExchange)
                .with(DEVICE_ROUTING_KEY);
    }

    @Bean
    public Queue monitoringMicroserviceQueue() {
        return new Queue(MONITORING_QUEUE_NAME, true);
    }

    @Bean
    public Binding monitoringQueueBinding(TopicExchange topicExchange, Queue monitoringMicroserviceQueue) {
        return BindingBuilder
                .bind(monitoringMicroserviceQueue)
                .to(topicExchange)
                .with(MONITORING_ROUTING_KEY);
    }
}
