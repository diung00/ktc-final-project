package com.example.ChoiGangDeliveryApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views")
public class ViewController {
    //home page
    @GetMapping("")
    public String showViewsPage() {
        return "views";
    }
    //login
    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "login/find-password";
    }
    //test
    @GetMapping("/driver-login-test")
    public String showDriverLoginPage() {
        return "driver-login-test";
    }
    //test
    @GetMapping("/map")
    public String map(){
        return "map";
    }

    @GetMapping("/get-my-profile")
    public String myProfile(){
        return "users/get-my-profile";
    }

    @GetMapping("/change-password")
    public String changePassword(){
        return "users/change-password";
    }

    @GetMapping("/reset-password")
    public String resetPassword(){
        return "users/reset-password";
    }

    @GetMapping("/update-profile")
    public String updateProfile(){
        return "users/update-profile";
    }
    @GetMapping("upload-profile-image")
    public String uploadProfileImage(){return "users/upload-profile-image";}

    @GetMapping("/request-driver-role")
    public String requestDriverRole(){
        return "users/request-driver-role";
    }

    @GetMapping("/request-owner-role")
    public String requestBusinessRole(){
        return "users/request-owner-role";
    }


    @GetMapping("/driver-request-status")
    public String driverRequestStatus(){return "users/driver-request-status";}

    @GetMapping("/owner-request-status")
    public String ownerRequestStatus(){return "users/owner-request-status";}

    @GetMapping("current-order")
    public String currentOrder(){return "users/current-order";}


    @GetMapping("/list-restaurant")
    public String listRestaurant(){return "restaurant/list-restaurant";}

    @GetMapping("/get-one-restaurant")
    public String getOneRestaurant(){return "restaurant/restaurant-details";}

    //owner
    @GetMapping("/restaurant-management")
    public String restaurantManagement() {return "restaurant/restaurant-management";}
    @GetMapping("/restaurant-update")
    public String restaurantUpdate() {return "restaurant/restaurant-update";}
    @GetMapping("/restaurant-open")
    public String openRestaurant() {return "restaurant/restaurant-create";}
    @GetMapping("/my-restaurant-requests")
    public String myRestaurantRequests() {return "restaurant/restaurant-view-requests";}

    //Menu
    @GetMapping("/menu")
    public String viewMenu() {return "menu/restaurant-menu";}
    @GetMapping("/restaurant-add-menu")
    public String addMenu() {return "menu/create-menu";}
    @GetMapping("/restaurant-update-menu")
    public String updateMenu() {return "menu/restaurant-update-menu";}

    //Admin dashboard
    @GetMapping("/admin")
    public String admin() {return "admin/admin";}

    //driver
    @GetMapping("/driver")
    public String driver() {return "driver/driver-dashboard";}


}
