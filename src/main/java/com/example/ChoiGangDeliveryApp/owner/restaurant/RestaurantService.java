package com.example.ChoiGangDeliveryApp.owner.restaurant;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.NaviService;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.PointDto;
import com.example.ChoiGangDeliveryApp.common.file.FileService;
import com.example.ChoiGangDeliveryApp.enums.ApprovalStatus;
import com.example.ChoiGangDeliveryApp.enums.CuisineType;
import com.example.ChoiGangDeliveryApp.enums.RequestType;
import com.example.ChoiGangDeliveryApp.enums.UserRole;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantOpenDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.dto.RestaurantUpdateDto;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantRequestEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.entity.RestaurantsEntity;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRepository;
import com.example.ChoiGangDeliveryApp.owner.restaurant.repo.RestaurantRequestRepository;
import com.example.ChoiGangDeliveryApp.security.config.AuthenticationFacade;
import com.example.ChoiGangDeliveryApp.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {
    private final AuthenticationFacade facade;
    private final RestaurantRepository restaurantRepo;
    private final NaviService naviService;
    private final FileService fileService;
    private final RestaurantRequestRepository restaurantRequestRepo;

    // Open Restaurant Request
    // Owner must fill some essential info for requesting to open a restaurant
    @Transactional
    public RestaurantDto openRestaurant(RestaurantOpenDto dto) {
        //Get current user
        UserEntity currentUser = facade.getCurrentUserEntity();

        // check user role
        if (!currentUser.getRole().equals(UserRole.ROLE_OWNER)
                || !currentUser.getApprovalStatus().equals(ApprovalStatus.APPROVED))
        {
            throw new IllegalStateException("User does not have permission to open a restaurant.");
        }

        //Check if this user already has a restaurant
        if (currentUser.getRestaurant() != null) {
            throw new IllegalArgumentException("User already has a restaurant.");
        }
        //check essential info
        validateRestaurantOpenDto(dto);

        // Exchange address to coordinates by NaviService
        PointDto locationPoint = naviService.geoCoding(dto.getAddress());

        // Create a new restaurant from RestaurantDto
        RestaurantsEntity newRestaurant = RestaurantsEntity.builder()
                .owner(currentUser)
                .name(dto.getName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .openingHours(dto.getOpeningHours())
                .cuisineType(CuisineType.valueOf(dto.getCuisineType()))
                .rating(0.0) // Set default rating to 0.0 for new restaurant
                .description(dto.getDescription())
                .latitude(locationPoint.getLatitude())
                .longitude(locationPoint.getLongitude())
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
        // Save the new restaurant
        restaurantRepo.save(newRestaurant);

        // Create a new restaurant request
        RestaurantRequestEntity request = RestaurantRequestEntity.builder()
                .restaurant(newRestaurant)
                .requestType(RequestType.OPEN)
                .status(ApprovalStatus.PENDING).build();

        // Save request
        RestaurantRequestEntity savedRequest = restaurantRequestRepo.save(request);
        return RestaurantDto.fromEntity(newRestaurant);
    }

    // Method to check essential info
    private void validateRestaurantOpenDto(RestaurantOpenDto dto) {
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required.");
        }
        if (dto.getAddress() == null || dto.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant address is required.");
        }
        if (dto.getPhone() == null || dto.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant phone number is required.");
        }
        if (dto.getOpeningHours() == null || dto.getOpeningHours().trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant opening hours are required.");
        }
        if (dto.getCuisineType() == null || dto.getCuisineType().trim().isEmpty()) {
            throw new IllegalArgumentException("Cuisine type is required.");
        }
    }
    // Update Restaurant Image
    public RestaurantDto updateProfileImg(Long restaurantId, MultipartFile image) throws Exception {
        // 1. Get current user
        UserEntity currentUser = facade.getCurrentUserEntity();

        // 2. Check if the restaurant exists
        Optional<RestaurantsEntity> restaurantOpt = restaurantRepo.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }
        // 3. Check if user is restaurant owner
        RestaurantsEntity restaurant = restaurantOpt.get();
        if (!restaurant.getOwner().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is not the owner of this restaurant.");
        }

        // 4. Determine the upload directory for the profile image
        String restaurantDirectory = "media/restaurants/" + restaurantId + "/"; // media/restaurants/{restaurantId}

            // Check and create the profile image folder if it doesn't exist
        try {
            Files.createDirectories(Path.of(restaurantDirectory));
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating profile directory");
        }
        // 5. Save img by FileService and get saved name
        String savedFileName = fileService.uploadFile(restaurantDirectory, image.getOriginalFilename(), image.getBytes());

        // 6.  set RestImage
        String reqPath = "/static/restaurants/" + restaurantId + "/" + savedFileName;
        restaurant.setRestImage(reqPath);

        // 7. save and return RestaurantDto
        return RestaurantDto.fromEntity(restaurantRepo.save(restaurant));

    }
    // Update restaurant information
    @Transactional
    public RestaurantDto updateRestaurantInfo(Long restaurantId, RestaurantUpdateDto dto) {
        // 1. Get current user
        UserEntity currentUser = facade.getCurrentUserEntity();

        // 2. Check if the restaurant exists
        Optional<RestaurantsEntity> restaurantOpt = restaurantRepo.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Restaurant not found");
        }

        // 3. Check if user is restaurant owner
        RestaurantsEntity restaurant = restaurantOpt.get();
        if (!restaurant.getOwner().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of this restaurant.");
        }
        // Exchange updated address to coordinates by NaviService
        PointDto locationPoint = naviService.geoCoding(dto.getAddress());

        // 4. Update restaurant information based on the provided DTO
        restaurant.setName(dto.getName());
        restaurant.setAddress(dto.getAddress());
        restaurant.setPhone(dto.getPhone());
        restaurant.setOpeningHours(dto.getOpeningHours());
        restaurant.setCuisineType(CuisineType.valueOf(dto.getCuisineType()));
        restaurant.setLongitude(locationPoint.getLongitude());
        restaurant.setLatitude(locationPoint.getLatitude());
        restaurant.setDescription(dto.getDescription());

        // 5. Save updated restaurant entity and return RestaurantDto
        return RestaurantDto.fromEntity(restaurantRepo.save(restaurant));
    }

    // Close Restaurant Request
    @Transactional
    public void closeRestaurant(Long restaurantId) {
        // 1. Get current user
        UserEntity currentUser = facade.getCurrentUserEntity();

        // 2. Check if the restaurant exists
        Optional<RestaurantsEntity> restaurantOpt = restaurantRepo.findById(restaurantId);
        if (restaurantOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found");
        }

        // 3. Check if user is restaurant owner
        RestaurantsEntity restaurant = restaurantOpt.get();
        if (!restaurant.getOwner().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not the owner of this restaurant.");
        }

        // 4. Only approvalStatus is 'APPROVED' can create a close shop request.
        if (!restaurant.getApprovalStatus().equals(ApprovalStatus.APPROVED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only restaurant status is approved can be closed");
        }

        // 5. create close request
        RestaurantRequestEntity request = RestaurantRequestEntity.builder()
                .restaurant(restaurant)
                .requestType(RequestType.CLOSE)
                .status(ApprovalStatus.PENDING)
                .build();

        // 5. save request
        restaurantRequestRepo.save(request);

        // 6. change restaurant approval status from approved to pending when waiting for close request approval
        restaurant.setApprovalStatus(ApprovalStatus.PENDING);

        // 7. save restaurant
        restaurantRepo.save(restaurant);
    }




    // View list restaurant by cuisine type and location around user location
    // Search Restaurant by userLocation and keyword






}
