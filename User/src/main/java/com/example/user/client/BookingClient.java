package com.example.user.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.user.dto.BookingDTO;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/{userId}")
    public List<BookingDTO> getBookingsByUserId(
            @PathVariable("userId") Long userId);

    // @PostMapping("/{userId}/booking-notifications")
    // void sendBookingNotification(
    //         @PathVariable("userId") Long userId,
    //         @RequestBody String bookingData);
}
