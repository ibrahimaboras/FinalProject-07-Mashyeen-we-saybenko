package com.example.Booking.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://localhost:8080/api/users")
public interface UserServiceClient {

    @PostMapping("/{userId}/booking-notifications")
    void sendBookingNotification(
            @PathVariable("userId") Long userId,
            @RequestBody String bookingData);
}