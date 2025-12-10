package com.l7aur.loadbalancermicroservice.config;

import com.l7aur.loadbalancermicroservice.model.IngestionQueueManager;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.IntStream;

@Configuration
public class IngestionTopicConfig {

    @Value("${ingestion.exchange.name}")
    private String INGESTION_EXCHANGE_NAME;

    @Value("${ingestion.microservice.queue.prefix}")
    private String INGESTION_QUEUE_NAME_PREFIX;

    @Bean
    public List<IngestionQueueManager> ingestionQueueManagers() {
        return List.of(
                new IngestionQueueManager(INGESTION_QUEUE_NAME_PREFIX + ".0","ingestion.queue.0"),
                new IngestionQueueManager(INGESTION_QUEUE_NAME_PREFIX + ".1","ingestion.queue.1"),
                new IngestionQueueManager(INGESTION_QUEUE_NAME_PREFIX + ".2","ingestion.queue.2")
        );
    }

    @Bean
    public RabbitAdmin ingestionRabbitAdmin(CachingConnectionFactory ingestionTopicConnectionFactory) {
        return new RabbitAdmin(ingestionTopicConnectionFactory);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(INGESTION_EXCHANGE_NAME);
    }

    @Bean
    public List<Queue> ingestionQueues() {
        return ingestionQueueManagers().stream()
                .map(qm -> new Queue(qm.queueName(), true))
                .toList();
    }

    @Bean
    public List<Binding> ingestionBindings(TopicExchange topicExchange, List<Queue> ingestionQueues, List<IngestionQueueManager> ingestionQueueManagers) {
        return IntStream.range(0, ingestionQueues.size())
                .mapToObj(i -> BindingBuilder
                        .bind(ingestionQueues.get(i))
                        .to(topicExchange)
                        .with(ingestionQueueManagers.get(i).routingKey()))
                .toList();
    }
}