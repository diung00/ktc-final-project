package com.example.ChoiGangDeliveryApp.owner.restaurant;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantOpenDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantRequestDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantUpdateDto;
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

    // View all restaurants within 2 km of the user
    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantDto>> getNearbyRestaurants(@RequestParam double latitude, @RequestParam double longitude) {
        double distance = 2.0; // 2 km
        List<RestaurantDto> nearbyRestaurants = restaurantService.getRestaurantsWithinRadius(latitude, longitude, distance);
        return ResponseEntity.ok(nearbyRestaurants);
    }

    // View a single restaurant by ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDto> getRestaurant(@PathVariable Long id) {
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        return ResponseEntity.ok(restaurant);
    }

    // View all restaurants within 2 km filtered by cuisine type
    @GetMapping("/nearby/cuisine")
    public ResponseEntity<List<RestaurantDto>> getNearbyRestaurantsByCuisineType(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam String cuisineType) {
        double distance = 2.0; // 2 km
        List<RestaurantDto> nearbyRestaurants = restaurantService.getRestaurantsWithinRadiusByCuisineType(latitude, longitude, distance, cuisineType);
        return ResponseEntity.ok(nearbyRestaurants);
    }


}
