package com.example.ChoiGangDeliveryApp;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("map")
public class MapTestController {

    @GetMapping("show")
    public String showMap() {
        return "map";
    }

}
