package com.l7aur.usermicroservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.usermicroservice.config.RabbitConfig;
import com.l7aur.usermicroservice.model.User;
import com.l7aur.usermicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class TopicPublisher {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final RabbitConfig config;
    private final UserRepository userRepository;

    public void requestDeviceServiceNewUser(String username) {
        record Message(TopicMessageType type, String username) {
        }

        try {
            sendMessageToDeviceMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_CREATE, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from user microservice: " + e.getMessage());
        }
    }

    public void requestDeviceServiceUpdateUser(String oldUsername, String newUsername) {
        record Message(TopicMessageType type, String oldUsername, String newUsername) {
        }

        try {
            sendMessageToDeviceMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_UPDATE, oldUsername, newUsername))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from user microservice: " + e.getMessage());
        }
    }

    public void requestDeviceServiceDeleteUsers(List<Integer> ids) {
        record Message(TopicMessageType type, List<String> usernames) {
        }
        List<String> usernames = ids.stream()
                .map(userRepository::findById)
                .map(u -> u.orElse(null))
                .filter(Objects::nonNull)
                .map(User::getUsername)
                .toList();
        try {
            sendMessageToDeviceMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_DELETE, usernames))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from user microservice: " + e.getMessage());
        }
    }

    private void sendMessageToDeviceMicroservice(String message) {
        try {
            rabbitTemplate.convertAndSend(config.getTOPIC_EXCHANGE_NAME(), config.getDEVICE_ROUTING_KEY(), message);
        } catch (AmqpException e) {
            System.out.println("Error while sending message from user microservice: " + e.getMessage());
        }
    }
}
