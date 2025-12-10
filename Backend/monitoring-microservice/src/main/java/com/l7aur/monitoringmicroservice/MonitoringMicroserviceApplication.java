package com.l7aur.monitoringmicroservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class MonitoringMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitoringMicroserviceApplication.class, args);
    }
}
