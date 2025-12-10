package com.l7aur.usermicroservice.service;

import com.l7aur.usermicroservice.model.User;
import com.l7aur.usermicroservice.model.delete.DeleteRequest;
import com.l7aur.usermicroservice.repository.TopicPublisher;
import com.l7aur.usermicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final TopicPublisher topicPublisher;

    public ResponseEntity<@NonNull List<User>> findAll() {
        try {
            return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<@NonNull User> findByUsername(String username) {
        try {
            Optional<User> user = userRepository.findByUsername(username);
            return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NO_CONTENT));
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void save(User user) {
        try {
            topicPublisher.requestDeviceServiceNewUser(user.getUsername());
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println("Failed to save user: " + user.getUsername() + "\n" + e.getMessage());
        }
    }

    public void update(String oldUsername, String newUsername) {
        try {
            Optional<User> oldUser = userRepository.findByUsername(oldUsername);
            if (oldUser.isEmpty())
                throw  new Exception("User: " + oldUsername + " not found");

            if (!oldUser.get().getUsername().equals(newUsername))
                topicPublisher.requestDeviceServiceUpdateUser(oldUser.get().getUsername(), newUsername);

            User newUser = oldUser.get();
            newUser.setUsername(newUsername);
            userRepository.save(newUser);
        }
        catch (Exception e) {
            System.out.println("Error " + e);
        }
    }

    @Transactional
    public void delete(DeleteRequest request) {
        try {
            if (!request.getIds().isEmpty()) {
                topicPublisher.requestDeviceServiceDeleteUsers(request.getIds());
                userRepository.deleteAllById(request.getIds());
            }
        }
        catch (Exception e) {
            System.out.println("Error " + e);
        }
    }
}
