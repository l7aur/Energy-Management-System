package com.l7aur.notificationmicroservice.service;

import com.auth0.jwt.JWT;
import com.l7aur.notificationmicroservice.config.MessageEncoder;
import com.l7aur.notificationmicroservice.config.NotificationConfigurator;
import com.l7aur.notificationmicroservice.model.Notification;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(
        value = "/ws",
        encoders = MessageEncoder.class,
        configurator = NotificationConfigurator.class
)
@Component
public class NotificationSocket {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        String token = (String) session.getUserProperties().get("token");
        String username = null;
        if (token != null && !token.isEmpty()) {
            try {
                username = JWT.decode(token).getSubject();
            } catch (Exception e) {
                System.err.println("Token validation failed: " + e.getMessage());
                session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Invalid token"));
                return;
            }
        }

        if (username != null) {
            System.out.println("Opened session: " + session.getId() + " associated with user: " + username);
            sessions.put(username, session);
            session.getUserProperties().put("username", username);
        } else {
            System.err.println("Session closed: Authentication required.");
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Authentication required"));
        }
    }

    @OnClose
    public void onClose(Session session) {
        String username = (String) session.getUserProperties().get("username");
        if (username != null) {
            sessions.remove(username);
            System.out.println("Closed session: " + session.getId() + ", removed user: " + username);
        } else {
            System.out.println("Closed session: " + session.getId() + ", user not mapped.");
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("Received: " + message);
        for (Session s : session.getOpenSessions()) {
            s.getBasicRemote().sendObject(new Notification("Notification", "Received big boss!"));
        }
    }

    public void sendNotification(final Notification notification) throws IOException, EncodeException {
        Session session = sessions.get(notification.getUsername());
        if (session != null && session.isOpen()) {
            session.getBasicRemote().sendObject(notification);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("I crashed [n. notification socket]");
        System.out.println("Error " + session.getId() + ": " + error.getMessage());
    }
}
