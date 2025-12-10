package com.l7aur.devicemicroservice.controller;

import com.l7aur.devicemicroservice.model.User;
import com.l7aur.devicemicroservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*")
@Tag(name = "user", description = "User management APIs to ensure consistency with the user microservice; implies data duplication")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @Operation(
            summary = "Returns all users",
            description = "Retrieves all users in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved all users in the database"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    ResponseEntity<@NonNull List<User>> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Returns a user",
            description = "Retrieves the user identified by its unique username in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the user from the database"),
                    @ApiResponse(responseCode = "404", description = "User does not exist"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    ResponseEntity<@NonNull User> findByUsername(@PathVariable(name = "username") String username) {
        return userService.findByUsername(username);
    }
}
