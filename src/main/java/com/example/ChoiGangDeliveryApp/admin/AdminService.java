package com.example.ChoiGangDeliveryApp.admin;

import com.example.ChoiGangDeliveryApp.common.exception.GlobalErrorCode;
import com.example.ChoiGangDeliveryApp.common.exception.GlobalException;
import com.example.ChoiGangDeliveryApp.driver.repo.DriverRepository;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.RequestType;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RejectOpenRestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantRequestDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRequestRepository;
import com.example.ChoiGangDeliveryApp.user.dto.DriverRoleRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.OwnerRoleRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import com.example.ChoiGangDeliveryApp.user.entity.DriverRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.OwnerRoleRequest;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import com.example.ChoiGangDeliveryApp.user.repo.DriverRoleRequestRepository;
import com.example.ChoiGangDeliveryApp.user.repo.OwnerRoleRequestRepository;
import com.example.ChoiGangDeliveryApp.user.repo.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final RestaurantRequestRepository restaurantRequestRepository;
    private final OwnerRoleRequestRepository ownerRoleRequestRepository;
    private final DriverRoleRequestRepository driverRoleRequestRepository;

    //View all users
    //xem tất cả user
    public List<UserDto> findAllUsers(){
         List<UserEntity> users = userRepository.findAll();
         return users.stream()
                 .map(UserDto::fromEntity)
                 .collect(Collectors.toList());
    }
    // View user by id
    //xem từng user
    public UserDto findUserById(Long id){
        Optional<UserEntity> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }
        return UserDto.fromEntity(user.get());
    }

    // view all user by role
    //xem tất cả user theo từng role
    public List<UserDto> findByRole(UserRole role){
        List<UserEntity> users = userRepository.findByRole(role);
        return users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }
    // ADMIN PROCESS ROLE REQUESTS

    //VIEW ALL REQUESTS
    //owner role requests
    public ResponseEntity<List<OwnerRoleRequestDto>> getAllOwnerRoleRequests() {
        List<OwnerRoleRequest> requests = ownerRoleRequestRepository.findAll();
        List<OwnerRoleRequestDto> requestDtos = requests.stream()
                .map(OwnerRoleRequestDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }
    //driver role requests
    @GetMapping("/driver-requests")
    public ResponseEntity<List<DriverRoleRequestDto>> getAllDriverRoleRequests() {
        List<DriverRoleRequest> requests = driverRoleRequestRepository.findAll();
        List<DriverRoleRequestDto> requestDtos = requests.stream()
                .map(DriverRoleRequestDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDtos);
    }

    //VIEW REQUEST BY ID
    // owner role request
    public OwnerRoleRequest getOwnerRoleRequestById(Long requestId) {
        return ownerRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));
    }
    //driver role request
    public DriverRoleRequest getDriverRoleRequestById(Long requestId) {
        return driverRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));
    }

    //ACCEPT REQUEST
    //owner role
    public ResponseEntity<String> acceptOwnerRoleRequest(
            @PathVariable Long requestId
    ) {
        OwnerRoleRequest request = ownerRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));

        UserEntity user = request.getUser();
        user.setRole(UserRole.ROLE_OWNER);
        userRepository.save(user);
        request.setStatus(ApprovalStatus.APPROVED);
        ownerRoleRequestRepository.save(request);
        return ResponseEntity.ok("Owner role request approved successfully.");
    }
    //driver role
    @PostMapping("/driver-requests/{requestId}/accept")
    public ResponseEntity<String> acceptDriverRoleRequest(
            @PathVariable Long requestId
    ) {
        DriverRoleRequest request = driverRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));

        UserEntity user = request.getUser();
        user.setRole(UserRole.ROLE_DRIVER);
        userRepository.save(user);

        request.setStatus(ApprovalStatus.APPROVED);
        driverRoleRequestRepository.save(request);

        return ResponseEntity.ok("Driver role request approved successfully.");
    }

    //DECLINE REQUEST
    //owner role
    @PostMapping("/owner-requests/{requestId}/decline")
    public ResponseEntity<String> declineOwnerRoleRequest(
            @PathVariable Long requestId,
            @RequestBody String reason
    ) {
        OwnerRoleRequest request = ownerRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));

        request.setStatus(ApprovalStatus.DECLINED);
        request.setRejectionReason(reason);
        ownerRoleRequestRepository.save(request);

        return ResponseEntity.ok("Owner role request declined successfully.");
    }
    //driver role
    @PostMapping("/driver-requests/{requestId}/decline")
    public ResponseEntity<String> declineDriverRoleRequest(
            @PathVariable Long requestId,
            @RequestBody String reason
    ) {
        DriverRoleRequest request = driverRoleRequestRepository.findById(requestId)
                .orElseThrow(() -> new GlobalException(GlobalErrorCode.REQUEST_NOT_FOUND));

        request.setStatus(ApprovalStatus.DECLINED);
        request.setRejectionReason(reason);
        driverRoleRequestRepository.save(request);

        return ResponseEntity.ok("Driver role request declined successfully.");
    }

    //ADMIN PROCESS RESTAURANT REQUEST

    //VIEW ALL RESTAURANT REQUEST WITH STATUS PENDING
    //xem yêu cầu mở hoặc đóng nhà hàng đang chờ xử lý
    public ResponseEntity<List<RestaurantRequestDto>> findPendingRequest(
            ApprovalStatus approvalStatus
    ){
        List<RestaurantRequestEntity> requests =
                restaurantRequestRepository.findByStatus(approvalStatus);
        return ResponseEntity.ok(requests.stream()
                .map(RestaurantRequestDto::fromEntity)
                .toList());
    }

    //VIEW ONE RESTAURANT REQUEST BY ID
    public ResponseEntity<RestaurantRequestDto> findRequestById(Long requestId) {
        RestaurantRequestEntity request = restaurantRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));
        return ResponseEntity.ok(RestaurantRequestDto.fromEntity(request));
    }


    //ACCEPT OPEN RESTAURANT REQUEST
    //chấp nhận mở nhà hàng
    @Transactional
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

    //DECLINE OPEN RESTAURANT REQUEST
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

    //ACCEPT TO CLOSE A RESTAURANT
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


}
