package com.example.ChoiGangDeliveryApp.driver;

import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {
    private final DriverService driverService;


    // UNAVAILABLE MODE
    @PutMapping("/{id}/unavailable")
    public ResponseEntity<Void> setDriverUnavailable(
            @PathVariable Long id
    ) {
        driverService.setDriverUnavailable(id);
        return ResponseEntity.ok().build();
    }

    // AVAILABLE MODE
    @PutMapping("/{id}/available")
    public ResponseEntity<Void> setDriverAvailable(
            @PathVariable Long id
    ) {
        driverService.setDriverAvailable(id);
        return ResponseEntity.ok().build();
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
