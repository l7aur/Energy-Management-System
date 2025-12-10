package com.l7aur.devicemicroservice.controller;

import com.l7aur.devicemicroservice.model.delete.DeleteReply;
import com.l7aur.devicemicroservice.model.delete.DeleteRequest;
import com.l7aur.devicemicroservice.model.Device;
import com.l7aur.devicemicroservice.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "device", description = "Device microservice management APIs")
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all devices",
            description = "Retrieves all devices in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all users"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while interrogating the database")
            }
    )
    public ResponseEntity<@NonNull List<Device>> findAll() {
        return deviceService.findAll();
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Get all devices linked to a username",
            description = "Retrieves all devices that belong to a certain username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of devices"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database, an empty list is returned")
            }
    )
    public ResponseEntity<@NonNull List<Device>> findByUsername(@PathVariable("username") String username) {
        return deviceService.findByUsername(username);
    }

    @PostMapping("/")
    @Operation(
            summary = "Save a new device in the database",
            description = "Creates a new device entry in the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Successfully created a new device in the database"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    public ResponseEntity<@NonNull Device> save(@RequestBody Device device) {
        return deviceService.save(device);
    }

    @PutMapping("/")
    @Operation(
            summary = "Update an already existing device",
            description = "Update a device that is already saved in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Device has been updated"),
                    @ApiResponse(responseCode = "404", description = "Device does not exist"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    public ResponseEntity<@NonNull Device> update(@RequestBody Device device) {
        return deviceService.update(device);
    }

    @DeleteMapping("/")
    @Operation(
            summary = "Delete one or more devices",
            description = "Delete one or more devices identified by their unique ids in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed the devices from the database"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    public ResponseEntity<@NonNull DeleteReply> delete(@RequestBody DeleteRequest request) {
        return deviceService.delete(request);
    }
}
