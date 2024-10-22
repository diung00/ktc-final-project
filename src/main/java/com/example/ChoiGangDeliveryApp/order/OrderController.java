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

    @PostMapping("create")
    public OrderDto createOrder(
            @RequestBody
            OrderDto orderDto
    ) {
        return orderService.createOrder(orderDto);
    }
    @PutMapping("approve")
    public OrderDto approveOrder(
            @RequestBody
            OrderDto orderDto
    ){
        return orderService.approveOrder(orderDto);
    }

    @PutMapping("getDriver")
    public OrderDto getDriver(
            @RequestBody
            OrderDto orderDto
    ){
        return orderService.getDriver(orderDto);
    }

    @PutMapping("customerCancelOrder")
    public ResponseEntity<String> customerCancelOrder(
            @RequestBody
            CancelOrderDto dto
    ){
        orderService.customerCancelOrder(dto);
        return ResponseEntity.ok("cancel order successful");
    }

    @PutMapping("restaurantCancelOrder")
    public ResponseEntity<String> restaurantCancelOrder(
            @RequestBody
            CancelOrderDto dto
    ){
        orderService.restaurantCancelOrder(dto);
        return ResponseEntity.ok("cancel order successful");
    }




}
