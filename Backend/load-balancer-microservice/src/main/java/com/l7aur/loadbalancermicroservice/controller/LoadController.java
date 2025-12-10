package com.l7aur.loadbalancermicroservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Tag(name = "LoadBalancer", description = "Load balancer microservice management APIs")
public class LoadController {

}
