package com.example.ChoiGangDeliveryApp.admin;

import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.RequestType;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RejectOpenRestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRequestRepository;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final DriverRepository driverRepository;
    private final RestaurantRequestRepository restaurantRequestRepository;

    //xem tất cả user
    public List<UserDto> findAllUsers(){
         List<UserEntity> users = userRepository.findAll();
         return users.stream()
                 .map(UserDto::fromEntity)
                 .collect(Collectors.toList());
    }

    //xem từng user
    public UserDto findUserById(Long id){
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return UserDto.fromEntity(user.get());
    }

    //xem tất cả user theo từng role
    public List<UserDto> findByRole(UserRole role){
        List<UserEntity> users = userRepository.findByRole(role);
        return users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    //xem yêu cầu mở hoặc đóng nhà hàng đang chờ xử lý

    public List<RestaurantRequestEntity> findPendingRequest(
            ApprovalStatus approvalStatus
    ){
        List<RestaurantRequestEntity> requests =
                restaurantRequestRepository.findByStatus(approvalStatus);
        return requests;
    }


    //chấp nhận mở nhà hàng
    public void approveOpenRestaurant(Long requestId){
        Optional<RestaurantRequestEntity> optionalRequest =
                restaurantRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RestaurantRequestEntity request = optionalRequest.get();
        if (request.getRequestType().equals(RequestType.OPEN)) {
            request.setStatus(ApprovalStatus.APPROVED);
            RestaurantsEntity restaurant = request.getRestaurant();
            restaurant.setApprovalStatus(ApprovalStatus.APPROVED);
            restaurantRepository.save(restaurant);
            restaurantRequestRepository.save(request);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //từ chối mở nhà hàng và nói lí do
    public RestaurantRequestEntity declineOpenRestaurant(RejectOpenRestaurantDto dto){
        Optional<RestaurantRequestEntity> optionalRequest =
                restaurantRequestRepository.findById(dto.getRequestId());
        if (optionalRequest.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RestaurantRequestEntity request = optionalRequest.get();
        if (request.getRequestType().equals(RequestType.OPEN)) {
            request.setStatus(ApprovalStatus.DECLINED);
            request.setRejectionReason(dto.getRejectReason());

            RestaurantsEntity restaurant = request.getRestaurant();
            restaurant.setApprovalStatus(ApprovalStatus.DECLINED);
            restaurantRepository.save(restaurant);
            restaurantRequestRepository.save(request);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return request;
    }


    //chấp nhận đóng nhà hàng
    public void approveCloseRestaurant(Long requestId){
        Optional<RestaurantRequestEntity> optionalRequest =
                restaurantRequestRepository.findById(requestId);
        if (optionalRequest.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        RestaurantRequestEntity request = optionalRequest.get();
        if (request.getRequestType().equals(RequestType.CLOSE)) {
            request.setStatus(ApprovalStatus.APPROVED);
            RestaurantsEntity restaurant = request.getRestaurant();
            restaurant.setApprovalStatus(ApprovalStatus.CLOSED);
            restaurantRepository.save(restaurant);
            restaurantRequestRepository.save(request);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    //xem yêu cầu trở thành driver
    //duyệt user thành driver
    //từ chối duyệt user thành driver






}
