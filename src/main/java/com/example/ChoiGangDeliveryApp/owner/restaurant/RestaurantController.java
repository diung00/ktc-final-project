package com.example.ChoiGangDeliveryApp.owner.restaurant;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantOpenDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantRequestDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantUpdateDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.user.dto.UserLocationDto;
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

    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS
    @GetMapping("/within-radius")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsWithinRadius(@RequestBody UserLocationDto userLocationDto) {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsWithinRadius(userLocationDto);
        return ResponseEntity.ok(restaurants);
    }

    // GET A SPECIFIC RESTAURANT BY ID
    @GetMapping("/{restaurantId}")
    public ResponseEntity<RestaurantDto> getRestaurantById(@PathVariable Long restaurantId) {
        RestaurantDto restaurant = restaurantService.getRestaurantById(restaurantId);
        return ResponseEntity.ok(restaurant);
    }

    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS FILTERED BY CUISINE TYPE
    @GetMapping("/within-radius/cuisine")
    public ResponseEntity<List<RestaurantDto>> getRestaurantsWithinRadiusByCuisineType(
            @RequestBody UserLocationDto userLocationDto,
            @RequestParam String cuisineType) {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsWithinRadiusByCuisineType(userLocationDto, cuisineType);
        return ResponseEntity.ok(restaurants);
    }

    // GET ALL RESTAURANTS WITHIN A GIVEN RADIUS FILTERED BY MENU NAME
    @GetMapping("/within-radius/menu")
    public ResponseEntity<List<RestaurantDto>> getAllRestaurantsWithin2KmByMenuName(
            @RequestBody UserLocationDto userLocationDto,
            @RequestParam String menuName) {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurantsWithin2KmByMenuName(userLocationDto, menuName);
        return ResponseEntity.ok(restaurants);
    }

}
