package com.l7aur.customersupportmicroservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.customersupportmicroservice.model.Message;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String encode(Message message) throws EncodeException {
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
