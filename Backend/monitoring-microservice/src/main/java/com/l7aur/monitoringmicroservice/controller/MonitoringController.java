package com.l7aur.monitoringmicroservice.controller;

import com.l7aur.monitoringmicroservice.model.SensorData;
import com.l7aur.monitoringmicroservice.service.MonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Tag(name = "Monitoring", description = "Monitoring microservice management APIs")
public class MonitoringController {
    private final MonitoringService monitoringService;

    @GetMapping("/{referencedDeviceId}")
    @Operation(
            summary = "Get sensor data for a device on a specific date",
            description = "Retrieve all sensor readings for a device identified by its ID for a specific date (format: yyyy-MM-dd)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Sensor readings have been found"),
                    @ApiResponse(responseCode = "204", description = "Device does not exist"),
                    @ApiResponse(responseCode = "400", description = "Missing or invalid date parameter"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    public ResponseEntity<List<SensorData>> findAllSensorData(
            @PathVariable("referencedDeviceId") Integer referencedDeviceId,
            @RequestParam(name = "date")
            @Parameter(description = "Date to filter sensor data (format: yyyy-MM-dd)", required = true)
            String dateStr) {
        return monitoringService.findByReferencedDeviceIdAndDate(referencedDeviceId, dateStr);
    }

}
