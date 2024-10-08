package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.order.dto.CancelOrderDto;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("create")
    public OrderDto createOrder(
            @RequestBody
            OrderDto dto
    ){
        return orderService.createOrder(dto);
    }

    @PostMapping("cancel")
    public OrderDto cancelOrder(
            @RequestBody
            CancelOrderDto dto
    ){
        return orderService.cancelOrder(dto);
    }

}
