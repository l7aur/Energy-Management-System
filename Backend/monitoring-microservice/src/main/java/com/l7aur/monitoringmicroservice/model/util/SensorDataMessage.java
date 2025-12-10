package com.l7aur.monitoringmicroservice.model.util;

import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.model.SensorData;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorDataMessage {
    private Timestamp timestamp;
    private Integer deviceId;
    private Double measuredValue;

    public SensorData toSensorData(Device device) {
        return new SensorData(
               null,
                LocalDateTime.of(timestamp.getYear(), timestamp.getMonth(), timestamp.getDay(),
                        timestamp.getHour(), timestamp.getMinute(), timestamp.getSecond()),
                device,
                measuredValue);
    }
}
