package com.l7aur.loadbalancermicroservice.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public CachingConnectionFactory rabbitConnectionFactory(
            @Value("${spring.rabbitmq.host}") String host,
            @Value("${spring.rabbitmq.port}") int port,
            @Value("${spring.rabbitmq.username}") String username,
            @Value("${spring.rabbitmq.password}") String password) {

        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory rabbitConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(rabbitConnectionFactory);
        return factory;
    }

    @Bean
    public CachingConnectionFactory ingestionTopicConnectionFactory(
            @Value("${spring.rabbitmq.ingestion.host}") String host,
            @Value("${spring.rabbitmq.ingestion.port}") int port,
            @Value("${spring.rabbitmq.ingestion.username}") String username,
            @Value("${spring.rabbitmq.ingestion.password}") String password) {

        CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
        factory.setUsername(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory ingestionTopicListenerFactory(ConnectionFactory ingestionTopicConnectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(ingestionTopicConnectionFactory);
        return factory;
    }

    @Bean
    public RabbitTemplate ingestionTopicRabbitTemplate(CachingConnectionFactory ingestionTopicConnectionFactory) {
        return new RabbitTemplate(ingestionTopicConnectionFactory);
    }
}
