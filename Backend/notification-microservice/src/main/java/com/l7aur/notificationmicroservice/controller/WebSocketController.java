package com.l7aur.notificationmicroservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Web Socket", description = "Web socket microservice management APIs")
public class WebSocketController {

}
