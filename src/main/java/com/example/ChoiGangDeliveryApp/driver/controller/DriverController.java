package com.example.ChoiGangDeliveryApp.driver.controller;

import com.example.ChoiGangDeliveryApp.driver.DriverService;
import com.example.ChoiGangDeliveryApp.driver.dto.DriverLocationDto;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
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

    @PostMapping("/update-location")
    public ResponseEntity<String> updateDriverLocation(@RequestBody DriverLocationDto dto) {
        driverService.updateDriverLocation(dto.getLatitude(), dto.getLongitude());
        return ResponseEntity.ok("Location has been updated successfully");
    }

    @GetMapping("/location")
    public ResponseEntity<DriverLocationDto> getDriverLocation(){
        DriverLocationDto location = driverService.getDriverLocation();
        return ResponseEntity.ok(location);
    }

    //Get driver status
    @GetMapping("/status")
    public ResponseEntity<String> getDriverStatus() {
        String status = driverService.getDriverStatus();
        return ResponseEntity.ok(status);
    }

    //DRIVER AVAILABLE MODE
    @PutMapping("/update-status")
    public ResponseEntity<String> setDriverStatus(@RequestParam String status) {
        try {
            driverService.setDriverStatus(DriverStatus.valueOf(status.toUpperCase()));
            return ResponseEntity.ok("Driver status updated to " + status);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value. Use 'AVAILABLE' or 'UNAVAILABLE'.");
        }
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
