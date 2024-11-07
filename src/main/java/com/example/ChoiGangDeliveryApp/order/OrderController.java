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

    //VIEW ALL ORDERS FOR USER
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDto>> viewMyOrder() {
        List<OrderDto> orders = orderService.viewMyOrder();
        return ResponseEntity.ok(orders);
    }

    //VIEW ALL ORDERS BY DRIVER
    @GetMapping("/driver")
    public ResponseEntity<List<OrderDto>> viewAllOrdersByDriverId() {
        List<OrderDto> orders = orderService.viewAllOrderByDriverId();
        return ResponseEntity.ok(orders);
    }

    // VIEW ALL ORDERS BY RESTAURANT FOR OWNER
    @GetMapping("/restaurant-orders")
    public ResponseEntity<List<OrderDto>> viewAllOrdersByRestaurant() {
        List<OrderDto> orders = orderService.viewAllOrdersByRestaurant();
        return ResponseEntity.ok(orders);
    }

    // RESTAURANT APPROVE AN ODER
    @PutMapping("/approve/{orderId}")
    public ResponseEntity<OrderDto>  approveOrder(
            @PathVariable Long orderId
    ){
        OrderDto approvedOrder = orderService.approveOrder(orderId);
        return ResponseEntity.ok(approvedOrder);
    }

    @PutMapping("/find-driver")
    public ResponseEntity<OrderDto> getDriver(
            @PathVariable Long orderId
    ){
        OrderDto updatedOrder = orderService.findDriver(orderId);
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
