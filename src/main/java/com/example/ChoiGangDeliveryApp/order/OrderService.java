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
import com.example.ChoiGangDeliveryApp.websocket.WebSocketService;
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
    private final WebSocketService webSocketService;

    // tạo một order, chưa có tài xế cho đơn này,
    // khi nào có tài xế thì setDriverId sau
    //create an order
    public OrderDto createOrder(OrderDto dto){
        UserEntity userEntity = authFacade.getCurrentUserEntity();
        if (!userEntity.getRole().equals(UserRole.ROLE_USER)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        RestaurantsEntity restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        OrderEntity orderEntity = OrderEntity.builder()
                .user(userEntity)
                .restaurant(restaurant)
                .orderDate(dto.getOrderDate())
                .orderStatus(OrderStatus.PAID)
                .totalMenusPrice(dto.getTotalMenuPrice())
                .shippingFee(dto.getShippingFee())
                .totalAmount(dto.getTotalAmount())
                .build();
        userRepository.save(userEntity);
        restaurantRepository.save(restaurant);
        webSocketService.sendOrderStatusToUser(userEntity.getId().toString(),"Your order has been created");
        webSocketService.sendOrderStatusToRestaurant(restaurant.getId().toString(),"New order!!!");
        return OrderDto.fromEntity(orderRepository.save(orderEntity));

    }

    //nhà hàng nhận đơn
    public OrderDto approveOrder(OrderDto dto){
        UserEntity userEntity = authFacade.getCurrentUserEntity();
        if (userEntity.getRestaurant() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have any restaurant");
        }

        OrderEntity orderEntity = orderRepository.findById(dto.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (!orderEntity.getRestaurant().equals(userEntity.getRestaurant())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This order does not belong to your restaurant");
        }

        if (!orderEntity.getOrderStatus().equals(OrderStatus.PAID)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order cannot be approved in its current state");
        }

        orderEntity.setOrderStatus(OrderStatus.COOKING);
        orderRepository.save(orderEntity);
        webSocketService.sendOrderStatusToUser(orderEntity.getUser().getId().toString(), "Your order has been approved");

        return OrderDto.fromEntity(orderEntity);

    }

    // gắn 1 tài xế cho 1 order
    //set driver for order
    public OrderDto getDriver (OrderDto orderDto) {
        UserEntity userEntity = authFacade.getCurrentUserEntity();
        if (!userEntity.getRole().equals(UserRole.ROLE_DRIVER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        DriverEntity driverEntity = userEntity.getDriver();
        Optional<OrderEntity> order = orderRepository.findById(orderDto.getId());
        if (!order.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }

        OrderEntity orderEntity = order.get();
        if (!orderEntity.getOrderStatus().equals(OrderStatus.WAITING_FOR_DRIVER)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        orderEntity.setDriver(driverEntity);
        driverRepository.save(driverEntity);
        webSocketService.sendOrderStatusToUser(userEntity.getId().toString(),"Catch order successfully!");
        // notification for driver
        webSocketService.sendOrderStatusToUser(orderEntity.getUser().getId().toString(),"Driver has caught your order!");
        return OrderDto.fromEntity(orderRepository.save(orderEntity));
    }


    // khách hàng hủy đơn khi nhà hàng chưa chấp nhận đơn
    // customer cancel own order
    public void customerCancelOrder(CancelOrderDto cancelOrderDto) {
        UserEntity user = authFacade.getCurrentUserEntity();

        OrderEntity orderToCancel = user.getOrders().stream()
                .filter(order -> order.getId().equals(cancelOrderDto.getOrderId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (orderToCancel.getOrderStatus().equals(OrderStatus.PAID)) {
            orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderToCancel);
            webSocketService.sendOrderStatusToUser(user.getId().toString(),"Order has been cancelled");
            webSocketService.sendOrderStatusToRestaurant(orderToCancel.getRestaurant().getId().toString(),"Order has been cancelled");

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled in its current state");
        }
    }

    // nhà hàng huỷ đơn vì 1 lí do nào đó
    // restaurant cancel an order with a reason
    public void restaurantCancelOrder(CancelOrderDto cancelOrderDto) {
        UserEntity owner = authFacade.getCurrentUserEntity();
        RestaurantsEntity restaurant = owner.getRestaurant();
        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }

        OrderEntity orderEntity = restaurant.getOrders().stream()
                .filter(order -> order.getId().equals(cancelOrderDto.getOrderId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (orderEntity.getOrderStatus().equals(OrderStatus.PAID) ||
                orderEntity.getOrderStatus().equals(OrderStatus.COOKING)) {
            orderEntity.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderEntity);
            webSocketService.sendOrderStatusToUser(orderEntity.getUser().getId().toString(),"Your order has been cancelled");
            webSocketService.sendOrderStatusToRestaurant(restaurant.getId().toString(),"Cancel order successfully");
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled");
        }
    }

    // tài xế huỷ bắt đơn

    // Driver xem order details
    public OrderDto getOrderDetails(Long orderId, Long driverId) {
        OrderEntity orderEntity = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (orderEntity.getDriver() == null || !orderEntity.getDriver().getId().equals(driverId)) {
            throw new RuntimeException("Driver is not authorized to view this order");
        }
        return OrderDto.fromEntity(orderEntity);
    }

    // Send order to driver
    public void assignOrderToDriver(Long orderId, Long driverId) {
        OrderEntity orderEntity = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found."));
        if (orderEntity.isAssigned()) {
            throw new RuntimeException("Order has already been assigned.");
        }
        DriverEntity driver = new DriverEntity();
        driver.setId(driverId);
        orderEntity.setDriver(driver);
        orderEntity.setAssigned(true);
        orderRepository.save(orderEntity);
    }

    // Driver accept order
    public void acceptOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        orderEntity.setOrderStatus(OrderStatus.DRIVER_ACCEPTED);
        orderRepository.save(orderEntity);
    }

    // Driver decline order
    public void declineOrder(Long orderId) {
        OrderEntity orderEntity = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        orderEntity.setOrderStatus(OrderStatus.WAITING_FOR_DRIVER);
        orderEntity.setDriver(null);
        orderRepository.save(orderEntity);
    }



}
