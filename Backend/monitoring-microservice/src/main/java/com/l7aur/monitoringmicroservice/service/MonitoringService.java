package com.l7aur.monitoringmicroservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.l7aur.monitoringmicroservice.config.EnergyManagementTopicConfig;
import com.l7aur.monitoringmicroservice.model.Device;
import com.l7aur.monitoringmicroservice.model.util.SensorDataMessage;
import com.l7aur.monitoringmicroservice.model.SensorData;
import com.l7aur.monitoringmicroservice.model.util.TopicMessageType;
import com.l7aur.monitoringmicroservice.repository.SensorDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@AllArgsConstructor
public class MonitoringService {
    private static final Integer NUMBER_OF_READINGS_PER_HOUR = 6;
    private final ObjectMapper objectMapper;
    private final SensorDataRepository sensorDataRepository;
    private final DeviceService deviceService;
    private final static Map<Integer, List<SensorData>> cache = new HashMap<>();
    private final RabbitTemplate rabbitTemplate;
    private final EnergyManagementTopicConfig configuration;

    @RabbitListener(queues = "${ingestion.queue.name}", containerFactory = "rabbitListenerContainerFactory")
    public void receiveMessage(String message) {
        try {
            System.out.println("[Monitoring Service] Received message: " + message);
            SensorDataMessage data = objectMapper.readValue(message, SensorDataMessage.class);
            Optional<Device> maybeDevice = deviceService.findByReferencedDeviceId(data.getDeviceId());
            if (maybeDevice.isEmpty()) {
                throw new Exception("Device with id: " + data.getDeviceId() + " not found");
            }

            SensorData sd = data.toSensorData(maybeDevice.get());
            if (!cache.containsKey(data.getDeviceId())) {
               cache.put(data.getDeviceId(), new ArrayList<>(List.of(sd)));
            }
            else {
                cache.get(data.getDeviceId()).add(sd);
            }

            if (cache.get(data.getDeviceId()).size() >= NUMBER_OF_READINGS_PER_HOUR) {
                sd = cache.get(data.getDeviceId()).getFirst();
                Double cumulativeReadings = cache.get(data.getDeviceId())
                        .stream()
                        .mapToDouble(SensorData::getMeasuredValue)
                        .sum();
                if (cumulativeReadings > maybeDevice.get().getMaximumConsumption()) {
                    record Message(TopicMessageType type, String username, String message) {
                    }
                    System.out.println("OVERCONSUMPTION!!!");
                    rabbitTemplate.convertAndSend(
                            configuration.getTOPIC_EXCHANGE_NAME(),
                            configuration.getNOTIFICATION_ROUTING_KEY(),
                            objectMapper.writeValueAsString(new Message(
                                    TopicMessageType.OVERCONSUMPTION,
                                    maybeDevice.get().getUser().getUsername(),
                                    "The recorded cumulative value " + cumulativeReadings + " is greater than the maximum consumption value: " + maybeDevice.get().getMaximumConsumption() + "!"
                            ))
                    );
                }
                sd.setMeasuredValue(cumulativeReadings);
                sensorDataRepository.save(sd);
                cache.remove(data.getDeviceId());
            }
        }
        catch (Exception e) {
            System.out.println("Error in receiving message: " + e.getMessage());
        }
    }

    public ResponseEntity<List<SensorData>> findByReferencedDeviceIdAndDate(
            Integer referencedDeviceId, String dateStr) {

        try {
            if (dateStr == null || dateStr.isBlank()) {
                return ResponseEntity.badRequest().build();
            }

            LocalDate date;
            try {
                date = LocalDate.parse(dateStr); // expects "yyyy-MM-dd"
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().build();
            }

            Optional<Device> maybeDevice = deviceService.findByReferencedDeviceId(referencedDeviceId);
            if (maybeDevice.isEmpty()) {
                return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NO_CONTENT);
            }

            List<SensorData> data = sensorDataRepository
                    .findAllByDevice_ReferencedDeviceIdAndTimestampBetween(
                            referencedDeviceId,
                            date.atStartOfDay(),
                            date.plusDays(1).atStartOfDay()
                    );

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Error fetching sensor data: " + e.getMessage());
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
