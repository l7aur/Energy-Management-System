package com.l7aur.customersupportmicroservice.controller;

import com.l7aur.customersupportmicroservice.model.Message;
import com.l7aur.customersupportmicroservice.service.CustomerSupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Customer Support", description = "Customer support microservice management APIs")
public class CustomerSupportController {
    private final CustomerSupportService customerSupportService;

    @PostMapping("/")
    @Operation(
        summary = "Ask Google's LLM questions",
        description = "Interact with Google's LLM, provides a brief context",
        responses = {
                @ApiResponse(responseCode = "200", description = "The answer has been successfully generated"),
                @ApiResponse(responseCode = "429", description = "The context is too big to be stored in memory, the current context is cleared"),
                @ApiResponse(responseCode = "500", description = "A problem has been encountered while generating the answer"),
        }
    )
    public ResponseEntity<@NonNull Message> askQuestion(@RequestBody Message question) {
        return customerSupportService.askQuestion(question);
    }
}
