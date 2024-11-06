package com.example.ChoiGangDeliveryApp.driver.controller;

import com.example.ChoiGangDeliveryApp.driver.DriverService;
import com.example.ChoiGangDeliveryApp.driver.dto.DriverLocationDto;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class DriverWebSocketController {
    private final DriverService driverService;

    public DriverWebSocketController(DriverService driverService) {
        this.driverService = driverService;
    }

//    @MessageMapping("/driver/location")
//    public void updateDriverLocation(DriverLocationDto driverLocationDto) {
//        driverService.updateDriverLocation(driverLocationDto.getIpAddress());
//    }
}
