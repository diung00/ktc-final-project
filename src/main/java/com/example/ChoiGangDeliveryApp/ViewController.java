package com.example.ChoiGangDeliveryApp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/views")
public class ViewController {
    @GetMapping("/driver-login-test")
    public String showDriverLoginPage() {
        return "driver-login-test";
    }
}
