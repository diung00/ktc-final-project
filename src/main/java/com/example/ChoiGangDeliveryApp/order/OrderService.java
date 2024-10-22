package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.driver.DriverService;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.order.dto.CancelOrderDto;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.order.repo.OrderRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.RestaurantService;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DriverRepository driverRepository;

//    // tạo một order, chưa có tài xế cho đơn này,
//    // khi nào có tài xế thì setDriverId sau
//    //create an order
//    public OrderDto createOrder(OrderDto dto){
//        UserEntity userEntity = authFacade.getCurrentUserEntity();
//        if (!userEntity.getRole().equals(UserRole.ROLE_USER)){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
//        RestaurantsEntity restaurant = restaurantRepository.findById(dto.getRestaurantId())
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
//        OrderEntity orderEntity = OrderEntity.builder()
//                .user(userEntity)
//                .restaurant(restaurant)
//                .orderDate(dto.getOrderDate())
//                .orderStatus(OrderStatus.PAID)
//                .totalMenusPrice(dto.getTotalMenuPrice())
//                .shippingFee(dto.getShippingFee())
//                .totalAmount(dto.getTotalAmount())
//                .build();
//        userRepository.save(userEntity);
//        restaurantRepository.save(restaurant);
//        return OrderDto.fromEntity(orderRepository.save(orderEntity));
//
//    }

    // gắn 1 tài xế cho 1 order
    //set driver for order
//    public OrderDto getDriver (OrderDto orderDto) {
//        UserEntity userEntity = authFacade.getCurrentUserEntity();
//        if (!userEntity.getRole().equals(UserRole.ROLE_DRIVER)) {
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
//        DriverEntity driverEntity = userEntity.getDriver();
//        Optional<OrderEntity> order = orderRepository.findById(orderDto.getId());
//        if (!order.isPresent()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
//        }
//
//        OrderEntity orderEntity = order.get();
//        if (!orderEntity.getOrderStatus().equals(OrderStatus.WAITING_FOR_DRIVER)){
//            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
//        }
//        orderEntity.setDriver(driverEntity);
//        driverRepository.save(driverEntity);
//        return OrderDto.fromEntity(orderRepository.save(orderEntity));
//    }


//    // khách hàng hủy đơn khi nhà hàng chưa chấp nhận đơn
//    // customer cancel own order
//    public void customerCancelOrder(CancelOrderDto cancelOrderDto) {
//        UserEntity user = authFacade.getCurrentUserEntity();
//
//        OrderEntity orderToCancel = user.getOrders().stream()
//                .filter(order -> order.getId().equals(cancelOrderDto.getOrderId()))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
//
//        if (orderToCancel.getOrderStatus().equals(OrderStatus.PAID)) {
//            orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
//
//            orderRepository.save(orderToCancel);
//        } else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled in its current state");
//        }
//    }

//    // nhà hàng huỷ đơn vì 1 lí do nào đó
//    // restaurant cancel an order with a reason
//    public void restaurantCancelOrder(CancelOrderDto cancelOrderDto) {
//        UserEntity owner = authFacade.getCurrentUserEntity();
//        RestaurantsEntity restaurant = owner.getRestaurant();
//        if (restaurant == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
//        }
//
//        OrderEntity orderEntity = restaurant.getOrders().stream()
//                .filter(order -> order.getId().equals(cancelOrderDto.getOrderId()))
//                .findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
//
//        if (orderEntity.getOrderStatus().equals(OrderStatus.PAID) ||
//                orderEntity.getOrderStatus().equals(OrderStatus.COOKING)) {
//            orderEntity.setOrderStatus(OrderStatus.CANCELLED);
//            orderRepository.save(orderEntity);
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled");
//        }
//    }

    // tài xế huỷ bắt đơn





}
