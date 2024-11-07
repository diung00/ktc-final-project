package com.example.ChoiGangDeliveryApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/order")
    public String order(){return "users/order";}

    @GetMapping("current-order")
    public String currentOrder(){return "users/current-order";}


    @GetMapping("/list-restaurant")
    public String listRestaurant(){return "restaurant/list-restaurant";}

    @GetMapping("/get-one-restaurant")
    public String getOneRestaurant(){return "restaurant/restaurant-details";}

    @GetMapping("/view-menu")
    public String viewMenuForUser(){return "menu/restaurant-menu-for-user";}

    //restaurant
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
    public String viewMenu() {return "menu/view-all-menus";}
    @GetMapping("/restaurant-add-menu")
    public String addMenu() {return "menu/create-menu";}
    @GetMapping("/restaurant-update-menu")
    public String updateMenu(@RequestParam("menuId") Long id, Model model) {
        model.addAttribute("menuId", id);
        return "menu/update-menu";
    }

    //order
    @GetMapping("/driver-orders")
    public String oderListByDriver() {return "order/orders-driver-view";}

    @GetMapping("/restaurant-orders")
    public String orderListByRestaurant() {return "order/orders-restaurant-view";}
    //Admin dashboard
    @GetMapping("/admin")
    public String admin() {return "admin/admin";}

    //driver
    @GetMapping("/driver")
    public String driver() {return "driver/driver-dashboard";}


}
