package com.example.Booking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "http://localhost:8081/User")
public interface UserServiceClient {
    @GetMapping("/users/{userId}/exists")
    boolean userExists(@PathVariable UUID userId);
}