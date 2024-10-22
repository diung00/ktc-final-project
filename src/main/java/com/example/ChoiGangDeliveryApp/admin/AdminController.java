package com.example.ChoiGangDeliveryApp.admin;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RejectOpenRestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantRequestDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.user.dto.DriverRoleRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.OwnerRoleRequestDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // View all users
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = adminService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    // view user by id
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = adminService.findUserById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    // view users by role
    @GetMapping("/users/role/{role}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable UserRole role) {
        List<UserDto> users = adminService.findByRole(role);
        return ResponseEntity.ok(users);
    }

    // view all owner role requests
    @GetMapping("/owner-requests")
    public ResponseEntity<List<OwnerRoleRequestDto>> getAllOwnerRoleRequests() {
        return adminService.getAllOwnerRoleRequests();
    }

    // view all driver role requests
    @GetMapping("/driver-requests")
    public ResponseEntity<List<DriverRoleRequestDto>> getAllDriverRoleRequests() {
        return adminService.getAllDriverRoleRequests();
    }

    // accept owner role request
    @PostMapping("/owner-requests/{requestId}/accept")
    public ResponseEntity<String> acceptOwnerRoleRequest(@PathVariable Long requestId) {
        return adminService.acceptOwnerRoleRequest(requestId);
    }

    // accept driver role request
    @PostMapping("/driver-requests/{requestId}/accept")
    public ResponseEntity<String> acceptDriverRoleRequest(@PathVariable Long requestId) {
        return adminService.acceptDriverRoleRequest(requestId);
    }

    // decline owner role request
    @PostMapping("/owner-requests/{requestId}/decline")
    public ResponseEntity<String> declineOwnerRoleRequest(
            @PathVariable Long requestId,
            @RequestBody String reason) {
        return adminService.declineOwnerRoleRequest(requestId, reason);
    }

    // decline driver role request
    @PostMapping("/driver-requests/{requestId}/decline")
    public ResponseEntity<String> declineDriverRoleRequest(
            @PathVariable Long requestId,
            @RequestBody String reason) {
        return adminService.declineDriverRoleRequest(requestId, reason);
    }

    // view all restaurant request
    @GetMapping("/restaurant-requests/pending")
    public ResponseEntity<List<RestaurantRequestDto>> getPendingRestaurantRequests() {
        return adminService.findPendingRequest(ApprovalStatus.PENDING);
    }

    // view restaurant request by Id
    @GetMapping("/restaurant-requests/{requestId}")
    public ResponseEntity<RestaurantRequestDto> getRestaurantRequestById(@PathVariable Long requestId) {
        return adminService.findRequestById(requestId);
    }

    // Accept to open restaurant
    @PostMapping("/restaurant-requests/{requestId}/approve/open")
    public ResponseEntity<String> approveOpenRestaurant(@PathVariable Long requestId) {
        adminService.approveOpenRestaurant(requestId);
        return ResponseEntity.ok("Open restaurant request approved successfully.");
    }

    // Decline to open restaurant
    @PostMapping("/restaurant-requests/decline/open")
    public ResponseEntity<RestaurantRequestEntity> declineOpenRestaurant(@RequestBody RejectOpenRestaurantDto dto) {
        RestaurantRequestEntity request = adminService.declineOpenRestaurant(dto);
        return ResponseEntity.ok(request);
    }

    // accept to close restaurant
    @PostMapping("/restaurant-requests/{requestId}/approve/close")
    public ResponseEntity<String> approveCloseRestaurant(@PathVariable Long requestId) {
        adminService.approveCloseRestaurant(requestId);
        return ResponseEntity.ok("Close restaurant request approved successfully.");
    }


}




















