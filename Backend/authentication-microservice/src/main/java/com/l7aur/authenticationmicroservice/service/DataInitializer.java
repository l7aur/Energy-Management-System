package com.l7aur.authenticationmicroservice.service;

import com.l7aur.authenticationmicroservice.model.Authentication;
import com.l7aur.authenticationmicroservice.model.util.Role;
import com.l7aur.authenticationmicroservice.repository.AuthenticationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AuthenticationRepository authenticationRepository, PasswordEncoder passwordEncoder) {
        return str -> {
            if (authenticationRepository.findAuthenticationByUsername("admin").isEmpty()) {
                Authentication admin = new Authentication(null, "admin", passwordEncoder.encode("admin"), null, Role.ADMIN);
                authenticationRepository.save(admin);
            }
        };
    }
}
