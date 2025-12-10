package com.l7aur.devicemicroservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.devicemicroservice.config.RabbitConfig;
import com.l7aur.devicemicroservice.model.util.TopicMessageType;
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
    private final RabbitConfig config;

    public void requestMonitoringServiceNewDevice(Integer id, Double maxConsumption, String username) {
        record Message(TopicMessageType type, Integer referencedId, Double maxConsumption, String username) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.DEVICE_CREATE, id, maxConsumption, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    public void requestMonitoringServiceUpdateDevice(Integer id, Double maxConsumption, String username) {
        record Message(TopicMessageType type, Integer referencedId, Double maxConsumption, String username) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.DEVICE_UPDATE, id, maxConsumption, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    public void requestMonitoringServiceDeleteUser(String username) {
        record Message(TopicMessageType type, String username) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_DELETE, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    public void requestMonitoringServiceCreateUser(String username) {
        record Message(TopicMessageType type, String username) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.USER_CREATE, username))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    public void requestMonitoringServiceDeleteDevices(List<Integer> ids) {
        record Message(TopicMessageType type, List<Integer> ids) {
        }

        try {
            sendMessageToMonitoringMicroservice(objectMapper.writeValueAsString(
                    new Message(TopicMessageType.DEVICE_DELETE, ids))
            );
        } catch (JsonProcessingException e) {
            System.out.println("Error while sending message from device microservice: " + e.getMessage());
        }
    }

    private void sendMessageToMonitoringMicroservice(String message) {
        try {
            rabbitTemplate.convertAndSend(config.getTOPIC_EXCHANGE_NAME(), config.getMONITORING_ROUTING_KEY(), message);
        } catch (AmqpException e) {
            System.out.println("Error while sending device message: " + e.getMessage());
        }
    }
}
