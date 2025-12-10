package com.l7aur.monitoringmicroservice.config;

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
public class IngestionQueueConfig {
    @Value("${ingestion.exchange.name}")
    private String INGESTION_EXCHANGE_NAME;

    @Value("${ingestion.queue.name}")
    private String INGESTION_QUEUE_NAME;

    @Value("${ingestion.routing.key}")
    private String INGESTION_ROUTING_KEY;

    @Bean
    public TopicExchange ingestionExchange() {
        return new TopicExchange(INGESTION_EXCHANGE_NAME);
    }

    @Bean
    public Queue ingestionQueue() {
        return new Queue(INGESTION_QUEUE_NAME, true);
    }

    @Bean
    public Binding ingestionBinding(TopicExchange ingestionExchange, Queue ingestionQueue) {
        return BindingBuilder
                .bind(ingestionQueue)
                .to(ingestionExchange)
                .with(INGESTION_ROUTING_KEY);
    }

    @Bean
    public RabbitAdmin queueAdmin(ConnectionFactory rabbitConnectionFactory) {
        return new RabbitAdmin(rabbitConnectionFactory);
    }
}
