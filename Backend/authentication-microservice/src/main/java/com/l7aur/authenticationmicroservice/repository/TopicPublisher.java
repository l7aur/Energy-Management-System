package com.l7aur.authenticationmicroservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.authenticationmicroservice.config.TopicConfig;
import com.l7aur.authenticationmicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class TopicPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final TopicConfig config;

    public void requestUserServiceNewUser(String username) {
        record Message(TopicMessageType type, String username) {
        }

        try {
            sendMessageToUserMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_CREATE, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending user message: " + e.getMessage());
        }
    }

    public void requestUserServiceUpdateUser(String oldUsername, String newUsername) {
        record Message(TopicMessageType type, String oldUsername, String newUsername) {
        }

        try {
            sendMessageToUserMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_UPDATE, oldUsername, newUsername))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending user message: " + e.getMessage());
        }
    }

    public void requestUserServiceDeleteUsers(List<Integer> ids) {
        record Message(TopicMessageType type, List<Integer> ids) {
        }

        try {
            sendMessageToUserMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_DELETE, ids))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending user message: " + e.getMessage());
        }
    }

    private void sendMessageToUserMicroservice(String message) {
        try {
            rabbitTemplate.convertAndSend(config.getTOPIC_EXCHANGE_NAME(), config.getUSER_ROUTING_KEY(), message);
        } catch (AmqpException e) {
            System.out.println("Error while sending user message: " + e.getMessage());
        }
    }
}
