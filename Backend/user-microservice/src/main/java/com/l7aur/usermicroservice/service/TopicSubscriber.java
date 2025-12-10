package com.l7aur.usermicroservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.usermicroservice.model.User;
import com.l7aur.usermicroservice.model.delete.DeleteRequest;
import com.l7aur.usermicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicSubscriber {
    private final ObjectMapper objectMapper;
    private final UserService userService;

    @RabbitListener(queues = "${user.microservice.queue}")
    private void receiveMessageFromAuthenticationQueue(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            TopicMessageType type = TopicMessageType.valueOf(root.get("type").asText());
            switch (type) {
                case USER_CREATE:
                    userService.save(new User(null, root.get("username").asText()));
                    break;
                case USER_UPDATE:
                    String oldUsername = root.get("oldUsername").asText();
                    String newUsername = root.get("newUsername").asText();
                    userService.update(oldUsername, newUsername);
                    break;
                case USER_DELETE:
                    List<Integer> ids = objectMapper.convertValue(root.get("ids"), new TypeReference<>() {
                    });
                    userService.delete(new DeleteRequest(ids));
                    break;
                default:
                    System.out.println("Invalid message received");
            }
        } catch (JsonProcessingException e) {
            System.out.println("Error while processing message: " + message + "\n" + e.getMessage());
        }
    }
}
