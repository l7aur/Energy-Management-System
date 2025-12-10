package com.l7aur.monitoringmicroservice.service;

import com.l7aur.monitoringmicroservice.model.User;
import com.l7aur.monitoringmicroservice.repository.DeviceRepository;
import com.l7aur.monitoringmicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final DeviceRepository deviceRepository;

    public void create(User user) {
        try {
            userRepository.save(user);
        }
        catch (Exception e) {
            System.out.println("[Monitoring Service] Error: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteByUsername(String username) {
        try {
            System.out.println("deleteByUsername: " + username);
            deviceRepository.deleteAllByUser_Username(username);
            userRepository.deleteByUsername(username);
        }
        catch (Exception e) {
            System.out.println("[Monitoring Service] Error: " + e.getMessage());
        }
    }
}
