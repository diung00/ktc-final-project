package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.order.dto.CancelOrderDto;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.order.repo.OrderRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final DriverRepository driverRepository;
    private final RestaurantRepository restaurantRepository;
    private final AuthenticationFacade authFacade;

    @Transactional
    public OrderDto createOrder (OrderDto dto){
        UserEntity user = authFacade.getCurrentUserEntity();
        RestaurantEntity restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found restaurant"));


        return OrderDto.fromEntity(orderRepository.save(OrderEntity.builder()
                .user(user)
                .restaurant(restaurant)
                .orderDate(dto.getOrderDate())
                .orderStatus(dto.getOrderStatus())
                .totalMenusOrderPrice(dto.getTotalMenuPrice())
                .shippingFee(dto.getShippingFee())
                .totalAmount(dto.getTotalAmount())
                .note(dto.getNote())
                .build()));
    }

    // hủy order khi chưa được chấp thuân
    public OrderDto cancelOrder(CancelOrderDto dto){
        UserEntity currentUser = authFacade.getCurrentUserEntity();
        OrderEntity order = orderRepository.findById(dto.getOrderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found order"));

        if (!order.getUser().getId().equals(currentUser.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "this is not your order");
        }
        else if (!order.getOrderStatus().equals(OrderStatus.PAID)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order can not cancel");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        return OrderDto.fromEntity(orderRepository.save(order));
    }

    // tài xế bắt đơn này
    public OrderDto driverCatchOrder(OrderDto dto){
        OrderEntity order = orderRepository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        UserEntity user = authFacade.getCurrentUserEntity();

        if (!user.getRole().equals(UserRole.ROLE_DRIVER)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you are not a driver");
        }

        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_DRIVER)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "order is not waiting for driver");
        }

        DriverEntity driver = user.getDriver();
        LocalDateTime acceptTime = LocalDateTime.now();

        order.setDriver(driver);
        order.setEstimatedArrivalTime(acceptTime.plusMinutes(30));
        order.setOrderStatus(OrderStatus.DRIVER_ACCEPT);

        return OrderDto.fromEntity(orderRepository.save(order));
    }

    //khách hàng xem lại giao dịch cũ



    //khách hàng xóa giao dịch cũ
    //
    //



}
