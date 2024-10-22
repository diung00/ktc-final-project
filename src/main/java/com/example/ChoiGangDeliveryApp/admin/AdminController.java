package com.example.ChoiGangDeliveryApp.admin;

import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RejectOpenRestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.user.dto.UserDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("findAllUsers")
    public List<UserDto> findAllUsers() {
        return adminService.findAllUsers();
    }

    @GetMapping("findUserById/{id}")
    public UserDto findUserById(
            @RequestParam("id")
            Long id
    ) {
        return adminService.findUserById(id);
    }

    @GetMapping("findByRole/{role}")
    public List<UserDto> findByRole(
            @RequestParam("role")
            UserRole role
    ){
        return adminService.findByRole(role);
    }

    @GetMapping("findPendingRequest/{approvalStatus}")
    public List<RestaurantRequestEntity> findPendingRequest(
            @RequestParam("approvalStatus")
            ApprovalStatus approvalStatus
    ){
        return adminService.findPendingRequest(approvalStatus);
    }

    @PutMapping("approveOpenRestaurant/{requestId}")
    public ResponseEntity<String> approveOpenRestaurant(
            @RequestParam("requestId")
            Long requestId
    ){
        adminService.approveOpenRestaurant(requestId);
        return ResponseEntity.ok("success!");
    }

    @PutMapping("declineOpenRestaurant")
    public RestaurantRequestEntity declineOpenRestaurant(
            @RequestBody
            RejectOpenRestaurantDto dto
    ){
        return adminService.declineOpenRestaurant(dto);
    }

    @PutMapping("approveCloseRestaurant/{id}")
    public ResponseEntity<String> approveCloseRestaurant(
            @RequestParam("id")
            Long requestId
    ){
        adminService.approveCloseRestaurant(requestId);
        return ResponseEntity.ok("success!");
    }


}




















