package com.l7aur.devicemicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.devicemicroservice.model.User;
import com.l7aur.devicemicroservice.model.delete.UserDeleteRequest;
import com.l7aur.devicemicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicSubscriber {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @RabbitListener(queues = "${device.microservice.queue}")
    private void receiveMessageFromAuthenticationQueue(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            TopicMessageType type = TopicMessageType.valueOf(root.get("type").asText());
            switch (type) {
                case USER_CREATE:
                    userService.save(new User(null, root.get("username").asText()));
                    break;
                case USER_UPDATE:
                    User oldUser = userService.findByUsername(root.get("oldUsername").asText()).getBody();
                    if (oldUser == null) {
                        System.out.println("No user found with username: " + root.get("oldUsername").asText() + "\nIgnoring...");
                        break;
                    }
                    oldUser.setUsername(root.get("newUsername").asText());
                    userService.save(oldUser);
                    break;
                case USER_DELETE:
                    List<String> usernames = objectMapper.convertValue(root.get("usernames"), new TypeReference<>() {
                    });
                    userService.delete(new UserDeleteRequest(usernames));
                    break;
                default:
                    System.out.println("Invalid message received");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error while processing message: " + message + "\n" + e.getMessage());
        }
    }
}
