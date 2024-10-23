package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.order.dto.CancelOrderDto;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2DeviceAuthorizationResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;
//
//    @PostMapping("create")
//    public OrderDto createOrder(
//            @RequestBody
//            OrderDto orderDto
//    ) {
//        return orderService.createOrder(orderDto);
//    }
//
//    @PutMapping("getDriver")
//    public OrderDto getDriver(
//            @RequestBody
//            OrderDto orderDto
//    ){
//        return orderService.getDriver(orderDto);
//    }
//
//    @PutMapping("customerCancelOrder")
//    public ResponseEntity<String> customerCancelOrder(
//            @RequestBody
//            CancelOrderDto dto
//    ){
//        orderService.customerCancelOrder(dto);
//        return ResponseEntity.ok("cancel order successful");
//    }
//
//    @PutMapping("restaurantCancelOrder")
//    public ResponseEntity<String> restaurantCancelOrder(
//            @RequestBody
//            CancelOrderDto dto
//    ){
//        orderService.restaurantCancelOrder(dto);
//        return ResponseEntity.ok("cancel order successful");
//    }
//

    // Driver xem order details
    @GetMapping("/{orderId}/driver/{driverId}")
    public ResponseEntity<OrderDto> getOrderDetails(
            @PathVariable Long orderId,
            @PathVariable Long driverId) {
        OrderDto orderDto = orderService.getOrderDetails(orderId, driverId);
        return ResponseEntity.ok(orderDto);
    }

    // Giao order cho driver
    @PostMapping("/{orderId}/assign/{driverId}")
    public ResponseEntity<Void> assignOrderToDriver(
            @PathVariable Long orderId,
            @PathVariable Long driverId
    ) {
        orderService.assignOrderToDriver(orderId, driverId);
        return ResponseEntity.ok().build();
    }

    // Driver accept order
    @PostMapping("/{orderId}/accept")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable Long orderId
    ) {
        orderService.acceptOrder(orderId);
        return ResponseEntity.ok().build();
    }

    // Driver decline order
    @PostMapping("/{orderId}/decline")
    public ResponseEntity<Void> declineOrder(
            @PathVariable Long orderId
    ) {
        orderService.declineOrder(orderId);
        return ResponseEntity.ok().build();
    }

}
