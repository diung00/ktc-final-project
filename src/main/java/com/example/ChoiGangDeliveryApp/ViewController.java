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
    //user
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

    //owner
    @GetMapping("/restaurant-management")
    public String restaurantManagement() {return "restaurant/restaurant-management";}


}
