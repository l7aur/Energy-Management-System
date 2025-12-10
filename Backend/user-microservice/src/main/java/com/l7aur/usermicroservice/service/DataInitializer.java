package com.l7aur.usermicroservice.service;

import com.l7aur.usermicroservice.model.User;
import com.l7aur.usermicroservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return str -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User(null, "admin");
                userRepository.save(admin);
            }
        };
    }
}
