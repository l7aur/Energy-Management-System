package com.l7aur.customersupportmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.customersupportmicroservice.config.MessageEncoder;
import com.l7aur.customersupportmicroservice.config.SpringContext;
import com.l7aur.customersupportmicroservice.model.Message;
import com.l7aur.customersupportmicroservice.service.CustomerSupportService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@ServerEndpoint(
        value = "/ws",
        encoders = MessageEncoder.class
)
@Component
public class CustomerSupportSocket {
    private CustomerSupportService customerSupportService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException {
        System.out.println("Opened session: " + session.getId());

        if (customerSupportService == null) {
            customerSupportService = SpringContext.getBean(CustomerSupportService.class);
        }

        for(Message m : customerSupportService.getMessages()) {
            session.getBasicRemote().sendObject(m);
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed session: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException, EncodeException {
        System.out.println("Received: " + message);

        Message msg = customerSupportService
                .askQuestion(objectMapper.readValue(message, Message.class))
                .getBody();

        for (Session s : session.getOpenSessions()) {
            if (!s.equals(session)) {
                s.getBasicRemote().sendObject(
                        objectMapper.readValue(message, Message.class));
            }
            
            s.getBasicRemote().sendObject(msg);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("I crashed [n. client support socket]");
        System.out.println("Error: " + error.getMessage());
    }
}
