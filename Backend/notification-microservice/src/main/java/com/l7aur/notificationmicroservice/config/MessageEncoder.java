package com.l7aur.notificationmicroservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.notificationmicroservice.model.Notification;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Notification> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Notification message) throws EncodeException {
        try {
            return mapper.writeValueAsString(message);
        } catch (Exception e) {
            throw new EncodeException(message, "Unable to encode message to JSON", e);
        }
    }

    @Override
    public void init(EndpointConfig config) {}

    @Override
    public void destroy() {}
}
