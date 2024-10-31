package com.example.ChoiGangDeliveryApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views")
public class ViewController {

    @GetMapping("")
    public String showViewsPage() {
        return "views";
    }

    @GetMapping("/login")
    public String login() {
        return "login/login";
    }

    @GetMapping("/find-password")
    public String findPassword() {
        return "login/find-password";
    }

    @GetMapping("/driver-login-test")
    public String showDriverLoginPage() {
        return "driver-login-test";
    }

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

    @GetMapping("/upload-profile-image")
    public String uploadProfileImage(){
        return "users/upload-profile-image";
    }

}
