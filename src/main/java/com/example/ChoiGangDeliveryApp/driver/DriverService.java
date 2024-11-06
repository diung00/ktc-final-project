package com.example.ChoiGangDeliveryApp.driver;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.NaviService;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.PointDto;
import com.example.ChoiGangDeliveryApp.driver.dto.DriverDto;
import com.example.ChoiGangDeliveryApp.driver.dto.DriverLocationDto;
import com.example.ChoiGangDeliveryApp.driver.entity.DriverEntity;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.DriverStatus;
import com.example.ChoiGangDeliveryApp.enums.OrderStatus;
import com.example.ChoiGangDeliveryApp.order.dto.OrderDto;
import com.example.ChoiGangDeliveryApp.order.entity.OrderEntity;
import com.example.ChoiGangDeliveryApp.order.repo.OrderRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverService {
    private final AuthenticationFacade facade;
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final NaviService naviService;

    private final SimpMessagingTemplate messagingTemplate;

    //update driver location by API geolocation
    public void updateDriverLocation(double latitude, double longitude) {
        UserEntity currentUser = facade.getCurrentUserEntity();
        DriverEntity driver = driverRepository.findByUser(currentUser)
                .orElseThrow(()->new IllegalArgumentException("Driver not found"));
        log.info("Driver found : {}", driver);
        driver.setLatitude(latitude);
        driver.setLongitude(longitude);
        driverRepository.save(driver);
    }

    //get driver location
    public DriverLocationDto getDriverLocation(){
        UserEntity currentUser = facade.getCurrentUserEntity();
        DriverEntity driver = driverRepository.findByUser(currentUser)
                .orElseThrow(()->new IllegalArgumentException("Driver not found"));
        DriverLocationDto dto = new DriverLocationDto();
        dto.setLatitude(driver.getLatitude());
        dto.setLongitude(driver.getLongitude());
        return dto;
    }

//    // get location of driver by IP
//    public void updateDriverLocation(String ipAddress) {
//        //current driver
//        UserEntity currentUser = facade.getCurrentUserEntity();
//
//        //find driver by current user
//        DriverEntity driver = driverRepository.findByUser(currentUser)
//                .orElseThrow(() -> new IllegalArgumentException("Driver not found for the current user"));
//        log.info("Driver found: {}", driver);
//        // get location from ipAddress
//        PointDto location = naviService.geoLocation(ipAddress);
//        log.info("Location from IP: " + location);
//
//        //update location
//        driver.setLatitude(location.getLatitude());
//        driver.setLongitude(location.getLongitude());
//
//        //save data
//        driverRepository.save(driver);
//        // sent location to clients
//        messagingTemplate.convertAndSend("/topic/driverLocation", new DriverDto(location.getLatitude(), location.getLongitude()));
//    }
    public String getDriverStatus() {
        UserEntity currentUser = facade.getCurrentUserEntity();
        DriverEntity driver = driverRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        return driver.getDriverStatus().name();

    }
    // AVAILABLE MODE
    public void setDriverStatus(DriverStatus status) {
        UserEntity currentUser = facade.getCurrentUserEntity();
        DriverEntity driver = driverRepository.findByUser(currentUser)
                .orElseThrow(() -> new RuntimeException("Driver not found"));
        driver.setDriverStatus(status);
        driverRepository.save(driver);
    }

    // VIEW ORDER(CANCELLED, COMPLETED)
    public List<OrderDto> getCompletedAndCancelledOrders(Long driverId) {
        List<OrderStatus> statuses = Arrays.asList(OrderStatus.COMPLETED, OrderStatus.CANCELLED);

        List<OrderEntity> orders = orderRepository.findByDriverIdAndOrderStatusIn(driverId, statuses);

        return orders.stream()
                .map(OrderDto::fromEntity)
                .collect(Collectors.toList());
    }
    //Search for directions to the delivery address

}
