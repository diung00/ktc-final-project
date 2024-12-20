package com.example.ChoiGangDeliveryApp.owner.restaurant;
import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantOpenDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantRequestDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantUpdateDto;
import com.example.ChoiGangDeliveryApp.user.dto.UserLocationDto;
import com.example.ChoiGangDeliveryApp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;

    // Endpoint for restaurant open request
    @PostMapping("/open")
    public ResponseEntity<String> openRestaurant(@RequestBody RestaurantOpenDto restaurantOpenDto) {
        restaurantService.openRestaurant(restaurantOpenDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Request to open restaurant successfully.");
    }

    // Endpoint for updating restaurant image
    @PutMapping("/{restaurantId}/update-image")
    public ResponseEntity<String> updateProfileImg(
            @PathVariable Long restaurantId,
            @RequestParam("image") MultipartFile image) throws Exception {
        restaurantService.updateProfileImg(restaurantId, image);
        return ResponseEntity.ok("Restaurant image updated successfully.");
    }

    // Endpoint for updating restaurant information
    @PutMapping("/{restaurantId}/update-info")
    public ResponseEntity<String> updateRestaurantInfo(
            @PathVariable Long restaurantId,
            @RequestBody RestaurantUpdateDto restaurantUpdateDto) {
        restaurantService.updateRestaurantInfo(restaurantId, restaurantUpdateDto);
        return ResponseEntity.ok("Restaurant information updated successfully.");
    }

    // Endpoint for closing restaurant request
    @DeleteMapping("/{restaurantId}/close")
    public ResponseEntity<String> closeRestaurant(@PathVariable Long restaurantId) {
        restaurantService.closeRestaurant(restaurantId);
        return ResponseEntity.ok("Request to close restaurant submitted successfully.");
    }

    // View all requests by the owner
    @GetMapping("/my-requests")
    public ResponseEntity<List<RestaurantRequestDto>> getMyRequests() {
        List<RestaurantRequestDto> requests = restaurantService.getMyRequests();
        return ResponseEntity.ok(requests);
    }

    // GET MY RESTAURANT
    @GetMapping("/my-restaurant")
    public ResponseEntity<RestaurantDto> getMyRestaurant(){
        RestaurantDto restaurant = restaurantService.getRestaurantByOwner();
        return ResponseEntity.ok(restaurant);
    }

    // GET A SPECIFIC RESTAURANT BY ID
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        RestaurantDto restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(restaurant);
    }
    //SEARCH RESTAURANT
    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS
    @GetMapping("/within-radius")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsWithinRadius() {
        UserLocationDto userLocationDto = userService.getUserLocation();
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsWithinRadius();
        return ResponseEntity.ok(restaurants);
    }


    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS FILTERED BY CUISINE TYPE
    @GetMapping("/within-radius/cuisine")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsWithinRadiusByCuisineType(
            @RequestParam String cuisineType) {
        CuisineType cuisineTypeEnum = CuisineType.valueOf(cuisineType);
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsWithinRadiusByCuisineType(cuisineTypeEnum);
        return ResponseEntity.ok(restaurants);
    }

    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS FILTERED BY MENU NAME WITH KEYWORD SEARCH
    @GetMapping("/within-radius/menu")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurantsWithin2KmByMenuName(
            @RequestParam String menuName) {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurantsWithinRadiusByMenuName(menuName);
        return ResponseEntity.ok(restaurants);
    }

}
