package com.l7aur.notificationmicroservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.notificationmicroservice.model.Notification;
import com.l7aur.notificationmicroservice.model.util.TopicMessageType;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationService {
    private final ObjectMapper objectMapper;
    private final NotificationSocket notificationSocket;

    @RabbitListener(queues = "${notification.queue.name}")
    public void receiveMessageFromNotificationQueue(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            TopicMessageType type = TopicMessageType.valueOf(root.get("type").asText());
            if (type == TopicMessageType.OVERCONSUMPTION) {
                final String username = root.get("username").asText();
                final String msg = root.get("message").asText();
                System.out.println("Publishing notification for " + username + ": " + msg);
                notificationSocket.sendNotification(new Notification(username, msg));
            } else {
                System.out.println("Invalid message received");
            }
        }
        catch (Exception e) {
            System.out.println("Error while processing message: " + message + "\n" + e.getMessage());
        }

    }
}
