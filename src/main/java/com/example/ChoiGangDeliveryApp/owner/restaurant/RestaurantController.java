package com.example.ChoiGangDeliveryApp.owner.restaurant;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantOpenDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

}
