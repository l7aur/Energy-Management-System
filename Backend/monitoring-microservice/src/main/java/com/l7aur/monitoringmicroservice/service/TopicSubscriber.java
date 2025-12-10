package com.l7aur.monitoringmicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.monitoringmicroservice.model.User;
import com.l7aur.monitoringmicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicSubscriber {
    private final DeviceService deviceService;
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @RabbitListener(queues = "${monitoring.microservice.queue}", containerFactory = "topicListenerFactory")
    private void receiveMessage(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            TopicMessageType type = TopicMessageType.valueOf(root.get("type").asText());
            switch (type) {
                case USER_CREATE:
                    userService.create(new User(null, root.get("username").asText()));
                    break;
                case USER_DELETE:
                    userService.deleteByUsername(root.get("username").asText());
                    break;
                case DEVICE_CREATE:
                    deviceService.create(null, root.get("referencedId").asInt(), root.get("maxConsumption").asDouble(), root.get("username").asText());
                    break;
                case DEVICE_UPDATE:
                    deviceService.update(root.get("referencedId").asInt(), root.get("maxConsumption").asDouble(), root.get("username").asText());
                    break;
                case DEVICE_DELETE:
                    List<Integer> ids = objectMapper.convertValue(root.get("ids"), new TypeReference<>() {
                    });
                    ids.forEach(deviceService::deleteByReferencedId);
                    break;
                default:
                    System.out.println("Invalid message received");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error while processing message: " + message + "\n" + e.getMessage());
        }
    }
}
