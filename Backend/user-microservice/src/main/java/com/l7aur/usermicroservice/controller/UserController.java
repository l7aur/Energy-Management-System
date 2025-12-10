package com.l7aur.usermicroservice.controller;

import com.l7aur.usermicroservice.model.User;
import com.l7aur.usermicroservice.service.UserService;
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
@Tag(name = "User", description = "User microservice management APIs")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @Operation(
            summary = "Get all users",
            description = "Return all users in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of all users in the database"),
                    @ApiResponse(responseCode = "500", description = "An error occurred, an empty list is returned")
            }
    )
    public ResponseEntity<@NonNull List<User>> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{username}")
    @Operation(
            summary = "Get a user by its username",
            description = "Retrieve a single user identified by its unique username",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User has been found"),
                    @ApiResponse(responseCode = "204", description = "User does not exist"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred while interrogating the database")
            }
    )
    public ResponseEntity<@NonNull User> getUser(@PathVariable("username") String username) {
        return userService.findByUsername(username);
    }
}
