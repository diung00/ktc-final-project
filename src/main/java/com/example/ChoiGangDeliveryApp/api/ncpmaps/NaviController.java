package com.example.ChoiGangDeliveryApp.api.ncpmaps;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.*;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("navigate")
@RequiredArgsConstructor
@Slf4j
public class NaviController {
    private final NaviService service;
    //Method use to take two coordinates and returns the movement route.
    @PostMapping("points")
    public NaviRouteDto withPoints(
            @RequestBody
            NaviWithPointsDto dto
    ) {
        log.info("controller dto : "+dto);
        return service.directions5(dto);
    }

    // Method use to take one coordinate as input and returns an address.
    @PostMapping("get-address")
    public RGeoResponseDto getAddress(
            @RequestBody
            PointDto point
    ) {
        return service.reverseGeocoding(point);
    }

    // A method that takes one coordinate and an address as input
    // and returns the movement route from the coordinate to the address search result location.
    @PostMapping("start-query")
    public NaviRouteDto withQuery(
            @RequestBody
            NaviWithQueryDto dto
    ) {
        return service.withQuery(dto);
    }

    // A method that takes two IP addresses as input and returns the movement route.
    @PostMapping("ips")
    public NaviRouteDto withIpAddresses(
            @RequestBody
            NaviWithIpsDto dto
    ) {
        return service.withIpAddresses(dto);
    }

    @GetMapping("geocode")
    public PointDto geocode(@RequestParam String query) {
        return service.geoCoding(query);
    }

}
