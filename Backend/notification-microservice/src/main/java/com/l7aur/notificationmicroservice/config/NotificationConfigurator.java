package com.l7aur.notificationmicroservice.config;

import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

import java.util.List;
import java.util.Map;

public class NotificationConfigurator extends ServerEndpointConfig.Configurator {

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        Map<String, List<String>> parameterMap = request.getParameterMap();
        List<String> tokens = parameterMap.get("token");
        if (tokens != null && !tokens.isEmpty()) {
            String token = tokens.get(0);
            sec.getUserProperties().put("token", token);
        }
        super.modifyHandshake(sec, request, response);
    }
}
