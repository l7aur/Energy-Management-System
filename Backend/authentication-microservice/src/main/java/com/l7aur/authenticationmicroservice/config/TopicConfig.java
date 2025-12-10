package com.l7aur.authenticationmicroservice.config;

import lombok.Getter;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TopicConfig {
    @Value("${topic.exchange.name}")
    private String TOPIC_EXCHANGE_NAME;

    @Value("${user.microservice.queue}")
    private String USER_QUEUE_NAME;

    @Value("${user.routing.key}")
    private String USER_ROUTING_KEY;

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    public Queue userMicroserviceQueue() {
        return new Queue(USER_QUEUE_NAME, true);
    }

    @Bean
    public Binding userQueueBinding(TopicExchange topicExchange, Queue userMicroserviceQueue) {
        return BindingBuilder
                .bind(userMicroserviceQueue)
                .to(topicExchange)
                .with(USER_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin topicAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
