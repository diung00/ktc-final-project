package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    //CREATE AN ORDER
    @PostMapping("/create")
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody
            OrderDto orderDto
    ) {
        OrderDto createdOrder = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    // VIEW 1 ORDER
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> viewOrder(@PathVariable Long orderId) {
        OrderDto orderDto = orderService.viewOrder(orderId);
        return ResponseEntity.ok(orderDto);
    }
    //VIEW ALL ORDERS BY RESTAURANT ID FOR DRIVER

    //VIEW ALL ORDERS BY DRIVER ID
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<OrderDto>> viewAllOrdersByDriverId(@PathVariable Long driverId) {
        List<OrderDto> orders = orderService.viewAllOrderByDriverId(driverId);
        return ResponseEntity.ok(orders);
    }

    // VIEW ALL ORDERS BY RESTAURANT ID FOR OWNER
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDto>> viewAllOrders(@PathVariable Long restaurantId) {
        List<OrderDto> orders = orderService.viewAllOrders(restaurantId);
        return ResponseEntity.ok(orders);
    }
    // RESTAURANT APPROVE AN ODER
    @PutMapping("/approve")
    public ResponseEntity<OrderDto>  approveOrder(
            @RequestBody
            OrderDto orderDto
    ){
        OrderDto approvedOrder = orderService.approveOrder(orderDto);
        return ResponseEntity.ok(approvedOrder);
    }

    @PutMapping("/get-driver")
    public ResponseEntity<OrderDto> getDriver(
            @RequestBody
            OrderDto orderDto
    ){
        OrderDto updatedOrder = orderService.getDriver(orderDto);
        return ResponseEntity.ok(updatedOrder);
    }
    //CANCEL AN ORDER
    // CUSTOMER CANCEL AN ORDER
    @PutMapping("/customer/{orderId}/cancel")
    public ResponseEntity<String> customerCancelOrder(
            @PathVariable Long orderId
    ){
        orderService.customerCancelOrder(orderId);
        return ResponseEntity.ok("cancel order successful");
    }
    //RESTAURANT CANCEL AN ORDER
    @PutMapping("/restaurant/{orderId}/cancel")
    public ResponseEntity<String> restaurantCancelOrder(
            @PathVariable Long orderId
    ){
        orderService.restaurantCancelOrder(orderId);
        return ResponseEntity.ok("cancel order successful");
    }

    // Driver decline order
    @PostMapping("/{orderId}/decline")
    public ResponseEntity<String> declineOrder(
            @PathVariable Long orderId,
            @RequestBody String reason
    ) {
        orderService.declineOrder(orderId, reason);
        return ResponseEntity.ok("Driver declined successful");
    }

}
