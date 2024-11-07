package com.example.ChoiGangDeliveryApp.order;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.NaviService;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.PointDto;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverDeclineLogEntity;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverDeclineLogRepository;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.order.repo.OrderMenuRepository;
import com.example.ChoiGangDeliveryApp.order.repo.OrderRepository;
import com.example.ChoiGangDeliveryApp.owner.menu.repo.MenuRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import com.example.ChoiGangDeliveryApp.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Driver;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final MenuRepository menuRepository;
    private final AuthenticationFacade authFacade;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DriverRepository driverRepository;
    private final WebSocketService webSocketService;
    private final DriverDeclineLogRepository declineLogRepository;

    //create an order
    public OrderDto createOrder(OrderDto dto){
        // current user
        UserEntity currentUser = authFacade.getCurrentUserEntity();

        //only user with ROLE_USER can create order
        if (!currentUser.getRole().equals(UserRole.ROLE_USER)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        //find restaurant by restaurant id
        RestaurantsEntity restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        // create order
        OrderEntity orderEntity = OrderEntity.builder()
                .user(currentUser)
                .deliveryAddress(currentUser.getAddress())
                .restaurant(restaurant)
                .orderStatus(OrderStatus.PAID)
                .totalMenusPrice(dto.getTotalMenusPrice())
                .shippingFee(dto.getShippingFee())
                .totalAmount(dto.getTotalAmount())
                .build();
        userRepository.save(currentUser);
        restaurantRepository.save(restaurant);
        webSocketService.sendOrderStatusToUser(currentUser.getId().toString(),"Your order has been created");
        webSocketService.sendOrderStatusToRestaurant(restaurant.getId().toString(),"New order!!!");
        return OrderDto.fromEntity(orderRepository.save(orderEntity));

    }

    // view one order by order id
    @Transactional(readOnly = true)
    public OrderDto viewOrder(Long orderId) {
        //get current user
        UserEntity currentUser = authFacade.getCurrentUserEntity();

        // find order by id
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        //find restaurant
        RestaurantsEntity restaurant = order.getRestaurant();

        boolean isOrderOwner = order.getUser().equals(currentUser);
        boolean isAssignedDriver = order.getDriver() != null && order.getDriver().getUser().equals(currentUser);
        boolean isOwner = restaurant.getOwner() != null && restaurant.getOwner().equals(currentUser);
        if (!isOrderOwner && !isAssignedDriver && !isOwner) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view this order");
        }
        return OrderDto.fromEntity(order);
    }

    //view all orders by restaurant id for driver

    //view all orders by driver id
    public List<OrderDto> viewAllOrderByDriverId() {
        //get current user
        UserEntity currentUser = authFacade.getCurrentUserEntity();
        Optional<DriverEntity> driverOpt = driverRepository.findByUser(currentUser);
        if (driverOpt.isEmpty()) {
            throw new RuntimeException("Driver not found for the current user");
        }
        DriverEntity driver =  driverOpt.get();
        //find order by driverId
        List<OrderEntity> orders = orderRepository.findAllByDriverId(driver.getId());

        return orders.stream().map(OrderDto::fromEntity).collect(Collectors.toList());
    }

    //View all orders by restaurant id for owner
    @Transactional(readOnly = true)
    public List<OrderDto> viewAllOrders(Long restaurantId) {
        //get current user
        UserEntity currentUser = authFacade.getCurrentUserEntity();

        // find restaurant by id
        RestaurantsEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        //check owner
        if (!restaurant.getOwner().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view orders for this restaurant");
        }

        // Find all orders by restaurant
        List<OrderEntity> orders = orderRepository.findAllByRestaurant(restaurant);

        return orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
    //Restaurant accept order
    //nhà hàng nhận đơn
    @Transactional
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
    // assign order to driver base on driver location
    // gắn 1 tài xế cho 1 order
    private static final double MAX_DISTANCE = 2000; //2000 meters (2 km)
    @Transactional
    public OrderDto getDriver (OrderDto orderDto) {

        UserEntity userEntity = authFacade.getCurrentUserEntity();
        if (!userEntity.getRole().equals(UserRole.ROLE_DRIVER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only drivers can pick up orders.");
        }

        Optional<OrderEntity> orderOptional = orderRepository.findById(orderDto.getId());
        if (orderOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found");
        }
        OrderEntity order = orderOptional.get();

        if (!order.getOrderStatus().equals(OrderStatus.WAITING_FOR_DRIVER)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Order is not waiting for a driver.");
        }
        //Get restaurant location
        PointDto restaurantLocation = new PointDto(
                order.getRestaurant().getLatitude(),
                order.getRestaurant().getLongitude()
        );

        // Get the list of drivers that satisfy the condition
        List<DriverEntity> eligibleDrivers = driverRepository.findAll().stream()
                .filter(driver -> driver.getOrdersCount() < 5)
                .toList();
        DriverEntity bestDriver = null;
        double closestDistance = Double.MAX_VALUE;
        for (DriverEntity driver : eligibleDrivers) {
            PointDto driverLocation = new PointDto(driver.getLatitude(), driver.getLongitude());
            double distance = calculateDistance(driverLocation, restaurantLocation);
            if (distance <= MAX_DISTANCE && distance < closestDistance) {
                closestDistance = distance;
                bestDriver = driver;
            }
        }
        if (bestDriver == null) {
            // Extend delivery time
            order.setEstimatedArrivalTime(calculateNewDeliveryTime(order.getEstimatedArrivalTime()));
            order.setOrderStatus(OrderStatus.DELIVERY_DELAYED);
            orderRepository.save(order);
            //send notification to restaurant and customer
            webSocketService.sendOrderStatusToRestaurant(order.getRestaurant().getId().toString(), "The order is ready but there is no driver to deliver it. Delivery times have been extended.");
            webSocketService.sendOrderStatusToUser(order.getUser().getId().toString(), "The order is ready but there is no driver to deliver it. Delivery times have been extended.");
            return orderDto;
        }
        // Assign driver to order
        order.setDriver(bestDriver);
        order.setOrderStatus(OrderStatus.ASSIGNED); // update status
        orderRepository.save(order);

        // send notification to driver
        webSocketService.sendOrderStatusToDriver(bestDriver.getUser().getId().toString(),
                "You have a new order to deliver!");
        // send notification to user
        webSocketService.sendOrderStatusToUser(order.getUser().getId().toString(),
                "Your order is being delivered!");
        webSocketService.sendOrderStatusToRestaurant(order.getRestaurant().getId().toString(),
                "We have found a driver for your order..");

        return OrderDto.fromEntity(order);
    }

    // calculate new delivery time if no driver satisfy condition
    private LocalDateTime calculateNewDeliveryTime(LocalDateTime currentDeliveryTime) {
        return currentDeliveryTime.plusMinutes(30);
    }

    public double calculateDistance(PointDto point1, PointDto point2) {
        final int R = 6371; // Radius of the Earth in kilometers

        double latDistance = Math.toRadians(point2.getLatitude() - point1.getLatitude());
        double lonDistance = Math.toRadians(point2.getLongitude() - point1.getLongitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(point1.getLatitude())) * Math.cos(Math.toRadians(point2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = R * c * 1000; // Convert to meters

        return distance; // return distance in meters
    }

    // method to process orders that delivery times have been extended
    @Transactional
    public void prioritizeDelayedOrders() {
        List<OrderEntity> delayedOrders = orderRepository.findByOrderStatus(OrderStatus.DELIVERY_DELAYED )
                .stream()
                .filter(this::isDeliveryTimeExtended)
                .toList();

        for (OrderEntity order : delayedOrders) {
            PointDto restaurantLocation = new PointDto(
                    order.getRestaurant().getLatitude(),
                    order.getRestaurant().getLongitude()
            );
            List<DriverEntity> eligibleDrivers = driverRepository.findAll().stream()
                    .filter(driver -> driver.getOrdersCount() < 5)
                    .toList();

            DriverEntity bestDriver = null;
            double closestDistance = Double.MAX_VALUE;
            for (DriverEntity driver : eligibleDrivers) {
                PointDto driverLocation = new PointDto(driver.getLatitude(), driver.getLongitude());
                double distance = calculateDistance(driverLocation, restaurantLocation);

                if (distance <= MAX_DISTANCE && distance < closestDistance) {
                    closestDistance = distance;
                    bestDriver = driver;
                }
            }

            if (bestDriver != null) {
                order.setDriver(bestDriver);
                order.setOrderStatus(OrderStatus.ASSIGNED);
                orderRepository.save(order);
                webSocketService.sendOrderStatusToDriver(bestDriver.getUser().getId().toString(),
                        "You have a new order to deliver!");
                webSocketService.sendOrderStatusToRestaurant(order.getRestaurant().getId().toString(),
                        "We have found a driver for your order..");
                webSocketService.sendOrderStatusToUser(order.getUser().getId().toString(),
                        "Your order is being delivered!");
            }
        }
    }

    private boolean isDeliveryTimeExtended(OrderEntity order) {
        return order.getEstimatedArrivalTime().isAfter(LocalDateTime.now());
    }

    // khách hàng hủy đơn khi nhà hàng chưa chấp nhận đơn
    // customer cancel order
    @Transactional
    public void customerCancelOrder(Long orderId) {
        UserEntity user = authFacade.getCurrentUserEntity();

        OrderEntity orderToCancel = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        //check authority
        if (!orderToCancel.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to cancel this order.");
        }
        //check order status
        if (orderToCancel.getOrderStatus().equals(OrderStatus.PAID) ||
                orderToCancel.getOrderStatus().equals(OrderStatus.PENDING)
        ) {
            //update order status
            orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderToCancel);

            //send notification
            webSocketService.sendOrderStatusToUser(user.getId().toString(),"Order has been cancelled");
            webSocketService.sendOrderStatusToRestaurant(orderToCancel.getRestaurant().getId().toString(),"Order has been cancelled");

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled");
        }
    }

    // nhà hàng huỷ đơn vì 1 lí do nào đó
    // restaurant cancel an order with a reason
    @Transactional
    public void restaurantCancelOrder(Long orderId) {
        UserEntity owner = authFacade.getCurrentUserEntity();
        RestaurantsEntity restaurant = owner.getRestaurant();

        if (restaurant == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
        //find order
        OrderEntity orderToCancel = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        //check order status
        if (orderToCancel.getOrderStatus().equals(OrderStatus.PENDING)
                || orderToCancel.getOrderStatus().equals(OrderStatus.PAID)
                || orderToCancel.getOrderStatus().equals(OrderStatus.COOKING)) {
            orderToCancel.setOrderStatus(OrderStatus.CANCELLED);
            orderRepository.save(orderToCancel);
            webSocketService.sendOrderStatusToUser(orderToCancel.getUser().getId().toString(),"Your order has been cancelled");
            webSocketService.sendOrderStatusToRestaurant(restaurant.getId().toString(),"Cancel order successfully");
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order cannot be cancelled");
        }
    }

    // Driver decline order
    public void declineOrder(Long orderId, String reason) {
        UserEntity currentUser = authFacade.getCurrentUserEntity();
        //find order
        OrderEntity orderEntity = orderRepository.findOrderById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        //check driver
        if (orderEntity.getDriver() == null || !orderEntity.getDriver().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not the assigned driver for this order.");
        }
        //check decline limit for today
        if (!canDriverDeclineOrder(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have exceeded the decline limit for today.");
        }
        // Set order status for waiting for another driver
        orderEntity.setOrderStatus(OrderStatus.WAITING_FOR_DRIVER);
        orderEntity.setDriver(null);
        orderRepository.save(orderEntity);
        // Log the decline
        logDriverDecline(currentUser.getId(), orderId, reason);
        // Notify users via WebSocket
        webSocketService.sendOrderStatusToDriver(orderEntity.getDriver().getId().toString(), "Declined successful.");
        webSocketService.sendOrderStatusToRestaurant(orderEntity.getRestaurant().getId().toString(), "Driver declined the order.");
    }
    private static final int MAX_DECLINE_COUNT = 3;
    public boolean canDriverDeclineOrder(Long driverId) {
        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        int declineCountToday = declineLogRepository.countDeclinesByDriverIdAndCreatedAtAfter(driverId, startOfDay);
        return declineCountToday < MAX_DECLINE_COUNT;
    }

    // save driver decline log
    public void logDriverDecline(Long driverId, Long orderId, String reason) {
        DriverDeclineLogEntity declineLog = new DriverDeclineLogEntity(driverId, orderId, reason);
        declineLogRepository.save(declineLog);
    }

}
