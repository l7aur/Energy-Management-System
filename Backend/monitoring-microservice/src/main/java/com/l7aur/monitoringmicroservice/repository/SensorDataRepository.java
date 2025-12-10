package com.l7aur.monitoringmicroservice.repository;

import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData,Integer> {
    void deleteAllByDevice(Device device);
    List<SensorData> findAllByDevice_ReferencedDeviceIdAndTimestampBetween(Integer referencedDeviceId, LocalDateTime localDateTime, LocalDateTime localDateTime1);
}
