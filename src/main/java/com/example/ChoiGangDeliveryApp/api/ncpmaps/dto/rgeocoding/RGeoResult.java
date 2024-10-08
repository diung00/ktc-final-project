package com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding;

import lombok.Data;

import java.util.Map;

@Data
public class RGeoResult {
    private String name;
    private Map<String, String> code;
    private RGeoRegion region;
    private RGeoLand land;
}
