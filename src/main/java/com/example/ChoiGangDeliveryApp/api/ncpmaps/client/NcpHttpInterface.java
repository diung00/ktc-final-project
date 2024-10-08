package com.example.ChoiGangDeliveryApp.api.ncpmaps.client;


import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.direction.DirectionNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.geocoding.GeoNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoNcpResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Map;

public interface NcpHttpInterface {
    // directions5
    @GetExchange("/map-direction/v1/driving")
    DirectionNcpResponse directions5(
            @RequestParam
            Map<String, Object> params
    );

    // geocode
    // convert address to coordinates
    @GetExchange("/map-geocode/v2/geocode")
    GeoNcpResponse geocode(
            @RequestParam
            Map<String, Object> params
    );

    // reverse geocode
    // exchange from coordinates to address
    @GetExchange("/map-reversegeocode/v2/gc")
    RGeoNcpResponse reverseGeocode(
            @RequestParam
            Map<String, Object> params
    );
}
