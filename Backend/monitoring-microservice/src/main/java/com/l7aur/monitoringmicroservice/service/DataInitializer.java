package com.l7aur.monitoringmicroservice.service;

import com.l7aur.monitoringmicroservice.model.User;
import com.l7aur.monitoringmicroservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return str -> {
            try {
                if (userRepository.findByUsername("admin").isEmpty()) {
                    User user = new User(null, "admin");
                    userRepository.save(user);
                }
            } catch (DataIntegrityViolationException e) {
                System.out.println("Admin user already created by another instance.");
            }
        };
    }
}
