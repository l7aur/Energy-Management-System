package com.l7aur.devicemicroservice.repository;

import com.l7aur.devicemicroservice.model.Device;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<@NonNull Device, @NonNull Integer> {
    @NonNull List<Device> findByUser_Username(String username);

    void deleteByUser_Username(String username);
}
