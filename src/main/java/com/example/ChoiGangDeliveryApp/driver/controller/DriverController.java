package com.example.ChoiGangDeliveryApp.driver.controller;

import com.example.ChoiGangDeliveryApp.driver.DriverService;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;

    @PostMapping("/updateLocation")
    public ResponseEntity<String> updateDriverLocation(@RequestParam String ip) {
        driverService.updateDriverLocation(ip);
        return ResponseEntity.ok("Location has been updated successfully.");
    }

    // UNAVAILABLE MODE
    @PutMapping("/unavailable")
    public ResponseEntity<String> setDriverUnavailable() {
        driverService.setDriverUnavailable();
        return ResponseEntity.ok("Driver status set to UNAVAILABLE");
    }

    // AVAILABLE MODE
    @PutMapping("/available")
    public ResponseEntity<String> setDriverAvailable() {
        driverService.setDriverAvailable();
        return ResponseEntity.ok("Driver status set to AVAILABLE");
    }

    // VIEW ORDER(CANCELLED, COMPLETED)
    @GetMapping("/{id}/orders/completed-cancelled")
    public ResponseEntity<List<OrderDto>> getCompletedAndCancelledOrders(
            @PathVariable Long id
//            @RequestParam List<OrderStatus> statuses

    ) {
        List<OrderDto> orders = driverService.getCompletedAndCancelledOrders(id);
        return ResponseEntity.ok(orders);
    }


}
