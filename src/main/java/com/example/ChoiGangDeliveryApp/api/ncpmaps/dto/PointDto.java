package com.example.ChoiGangDeliveryApp.api.ncpmaps.dto;

import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.direction.DirectionRoute;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PointDto {
    private Double longitude;
    private Double latitude;

    public String toQueryValue() {
        return String.format("%f,%f", longitude, latitude);
    }

    public static PointDto stringToPointDto(String s) {
        String[] split = s.split(",");
        PointDto pointDto = new PointDto(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        return pointDto;
    }

    public static List<PointDto> directionRouteToPointDto(DirectionRoute directionRoute) {
        List<PointDto> path = new ArrayList<>();
        for (int i = 0; i < directionRoute.getPath().size(); i++) {
            Double lng = directionRoute.getPath().get(i).get(0);
            Double lat = directionRoute.getPath().get(i).get(1);
            PointDto dto = new PointDto(lng, lat);
            path.add(dto);
        }
        return path;
    }
}
