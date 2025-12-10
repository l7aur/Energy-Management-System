package com.l7aur.authenticationmicroservice.controller;

import com.l7aur.authenticationmicroservice.model.Authentication;
import com.l7aur.authenticationmicroservice.model.delete.DeleteReply;
import com.l7aur.authenticationmicroservice.model.delete.DeleteRequest;
import com.l7aur.authenticationmicroservice.model.login.LoginReply;
import com.l7aur.authenticationmicroservice.model.login.LoginRequest;
import com.l7aur.authenticationmicroservice.model.register.RegisterReply;
import com.l7aur.authenticationmicroservice.model.register.RegisterRequest;
import com.l7aur.authenticationmicroservice.service.AuthenticationService;
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
@Tag(name = "Authentication", description = "Authentication microservice management APIs")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(
            summary = "Register as a new client in the system",
            description = "Create a new authentication entry in the database",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Registration process succeeded"),
                    @ApiResponse(responseCode = "400", description = "Registration process failed, check the error message in the reply")
            }
    )
    public ResponseEntity<@NonNull RegisterReply> register(@RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login in the system",
            description = "Authenticate a user in the system and links it to a newly created JWT",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful, the JWT is returned in the response"),
                    @ApiResponse(responseCode = "401", description = "Login failed because the credentials were wrong or the JWT service failed to create a token")
            }
    )
    public ResponseEntity<@NonNull LoginReply> login(@RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all authentications",
            description = "Retrieve all authentications in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Returns the list of all authentications has been retrieved successfully"),
                    @ApiResponse(responseCode = "500", description = "An error occurred while interrogating the database, an empty list is returned")
            }
    )
    public ResponseEntity<@NonNull List<Authentication>> findAll() {
        return authenticationService.findAll();
    }

    @PutMapping("/")
    @Operation(
            summary = "Updates an authentication",
            description = "Update a single already existing entry in the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success, the entry in the database after the request has been processed is returned"),
                    @ApiResponse(responseCode = "404", description = "The entry to update was not found"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred, the authentication provided in the request is returned")
            }
    )
    public ResponseEntity<@NonNull Authentication> update(@RequestBody Authentication authentication) {
        return authenticationService.update(authentication);
    }

    @DeleteMapping("/")
    @Operation(
            summary = "Delete one or more authentications",
            description = "Delete one or more authentications identified by ids from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully removed the specified entries in the database"),
                    @ApiResponse(responseCode = "500", description = "Some error occurred, check the attached error message")
            }
    )
    public ResponseEntity<@NonNull DeleteReply> delete(@RequestBody DeleteRequest request) {
        return authenticationService.delete(request);
    }

}
