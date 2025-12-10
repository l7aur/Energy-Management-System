package com.l7aur.loadbalancermicroservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.loadbalancermicroservice.model.IngestionQueueManager;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${ingestion.exchange.name}")
    private String INGESTION_EXCHANGE_NAME;

    private final RabbitTemplate ingestionRabbitTemplate;

    private final List<IngestionQueueManager> ingestionQueueManagers;

    @RabbitListener(queues = "${sensor.data.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(@Payload final String message) {
        try {
            System.out.println("[Load Balancer Service] Received message: " + message);

            JsonNode rootNode = objectMapper.readTree(message);
            JsonNode deviceIdNode = rootNode.get("deviceId");

            if (deviceIdNode == null || !deviceIdNode.isInt()) {
                throw new IllegalArgumentException("Message is malformed: 'deviceId' not found or is not an integer.");
            }

            if (ingestionQueueManagers.isEmpty()) {
                throw new IllegalStateException("Ingestion queue managers are not initialized.");
            }

            postMessage(message, mapMessageToIngestionQueueManager(deviceIdNode.asInt()));
        }
        catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void postMessage(final String message, final int managerIndex) {
        try {
            System.out.println("[Load Balancer Service] Posting message to: " + ingestionQueueManagers.get(managerIndex).routingKey() + ": " + message);
            ingestionRabbitTemplate.send(INGESTION_EXCHANGE_NAME, ingestionQueueManagers.get(managerIndex).routingKey(), new Message(message.getBytes()));
        }
        catch (AmqpException e) {
            System.out.println("Error in sending message to " + ingestionQueueManagers.get(managerIndex).routingKey() + ": " + e.getMessage());
        }
    }

    private int mapMessageToIngestionQueueManager(final int deviceId) {
        return deviceId % ingestionQueueManagers.size();
    }
}
