package com.l7aur.monitoringmicroservice.service;

import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.model.User;
import com.l7aur.monitoringmicroservice.repository.DeviceRepository;
import com.l7aur.monitoringmicroservice.repository.SensorDataRepository;
import com.l7aur.monitoringmicroservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;
    private final SensorDataRepository sensorDataRepository;
    private final UserRepository userRepository;

    public void create(Integer deviceId, Integer referencedId, Double maximumConsumption, String username) {
        try {
            Optional<User> u = userRepository.findByUsername(username);
            if (u.isEmpty())
                throw new Exception("Username not found");

            deviceRepository.save(new Device(
                    deviceId,
                    referencedId,
                    maximumConsumption,
                    u.get()
            ));
        }
        catch (Exception e) {
            System.out.println("Device: " + referencedId + " not saved! " + e.getMessage());
        }
    }

    public void update(Integer referencedId, Double maximumConsumption, String username) {
        try {
            Optional<Device> d = deviceRepository.findByReferencedDeviceId(referencedId);
            Optional<User> u = userRepository.findByUsername(username);
            if (d.isEmpty())
                throw new Exception("Device not found");

            if (u.isEmpty())
                throw new Exception("Username not found");

            d.get().setMaximumConsumption(maximumConsumption);
            d.get().setUser(u.get());
            deviceRepository.save(d.get());
        }
        catch (Exception e) {
            System.out.println("Device: " + referencedId + " not updated! " + e.getMessage());
        }
    }

    public Optional<Device> findByReferencedDeviceId(Integer deviceId) {
        return deviceRepository.findByReferencedDeviceId(deviceId);
    }

    @Transactional
    public void deleteByReferencedId(Integer referencedId) {
        try {
            Optional<Device> d = deviceRepository.findByReferencedDeviceId(referencedId);
            if (d.isEmpty()) {
                System.out.println("Device with id: " + referencedId + " not found!");
                return;
            }
            sensorDataRepository.deleteAllByDevice(d.get());
            deviceRepository.deleteDeviceByReferencedDeviceId(referencedId);
        }
        catch (Exception e) {
            System.out.println("Device identified by referenced id: " + referencedId + " not deleted!");
        }
    }
}
