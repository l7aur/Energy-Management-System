package com.l7aur.devicemicroservice.service;

import com.l7aur.devicemicroservice.model.User;
import com.l7aur.devicemicroservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        return str -> {
            if (userRepository.findUserByUsername("admin").isEmpty()) {
                User user = new User(null, "admin");
                userRepository.save(user);
            }
        };
    }
}
