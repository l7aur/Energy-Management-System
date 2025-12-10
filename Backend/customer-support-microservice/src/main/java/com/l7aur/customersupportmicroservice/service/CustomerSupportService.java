package com.l7aur.customersupportmicroservice.service;

import com.l7aur.customersupportmicroservice.model.Message;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.google.genai.GoogleGenAiChatModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerSupportService {
    private final ChatClient chatClient;
    static private final String myUsername = "Customer Support";
    private final List<org.springframework.ai.chat.messages.Message> context = new ArrayList<>();
    static private final Integer MAXIMUM_NUMBER_OF_PROMPTS_PER_CHAT = 100;
    @Getter
    private final List<Message> messages = new ArrayList<>();

    public CustomerSupportService(GoogleGenAiChatModel chatModel) {
        chatClient = ChatClient
                .builder(chatModel)
                .defaultAdvisors()
                .build();
    }

    public ResponseEntity<@NonNull Message> askQuestion(Message question) {
        try {
            if (context.size() >= MAXIMUM_NUMBER_OF_PROMPTS_PER_CHAT) {
                context.clear();
                return new  ResponseEntity<>(new Message(myUsername, "You asked too many questions!"), HttpStatus.TOO_MANY_REQUESTS);
            }

            messages.add(question);
            context.add(new UserMessage(question.getMessage()));
            Prompt prompt = new Prompt(context);

            String answer = chatClient.prompt(prompt)
                    .call()
                    .content();
            if (answer == null)
                throw new RuntimeException("Answer is null!");

            context.add(new AssistantMessage(answer));
            messages.add(new Message(myUsername, answer));

            return new ResponseEntity<>(new Message(myUsername, answer), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new Message(myUsername, "Sorry, this service is unavailable at the moment. Please check again later... " + e), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
