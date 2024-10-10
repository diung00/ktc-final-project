package com.example.ChoiGangDeliveryApp.api.ncpmaps;


import com.example.ChoiGangDeliveryApp.api.ncpmaps.client.NcpGeolocationInterface;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.client.NcpHttpInterface;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.*;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.direction.DirectionNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.direction.DirectionRoute;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.geocoding.GeoAddress;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.geocoding.GeoNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.geolocation.GeoLocationNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoLand;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoNcpResponse;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoRegion;
import com.example.ChoiGangDeliveryApp.api.ncpmaps.dto.rgeocoding.RGeoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaviService {
    private final NcpHttpInterface mapExchange;
    private final NcpGeolocationInterface geoExchange;

    public NaviRouteDto directions5(NaviWithPointsDto dto) {
        Map<String, Object> params = new HashMap<>();
        params.put("start", dto.getStart().toQueryValue());
        params.put("goal", dto.getGoal().toQueryValue());
        log.info("start & goal : {}, {}", dto.getStart(), dto.getGoal());

        DirectionNcpResponse response = mapExchange.directions5(params);
        log.info("response:: " + response);

        DirectionRoute route = response.getRoute().get("traoptimal").get(0);
        List<PointDto> path = PointDto.directionRouteToPointDto(route);
        NaviRouteDto result = new NaviRouteDto(path);

        return result;
    }

    public RGeoResponseDto reverseGeocoding(PointDto dto) {
        log.info("dto came : "+dto);

        Map<String, Object> params = new HashMap<>();
        params.put("coords", dto.toQueryValue());
        params.put("output", "json");
        params.put("orders", "roadaddr");
        log.info(params.toString());

        RGeoNcpResponse response = mapExchange.reverseGeocode(params);
        log.info("response :  " + response);
        RGeoRegion rGeoResultRegion = response.getResults().get(0).getRegion();
        RGeoLand rGeoResultLand = response.getResults().get(0).getLand();

        StringBuffer sb = new StringBuffer();
        sb.append(rGeoResultRegion.getArea1().getName()).append(" ");
        sb.append(rGeoResultRegion.getArea2().getName()).append(" ");
        sb.append(rGeoResultRegion.getArea3().getName()).append(" ");
        sb.append(rGeoResultLand.getName()).append(" ").append(rGeoResultLand.getNumber1());

        RGeoResponseDto result = new RGeoResponseDto(sb.toString());
        return result;
    }

    public NaviRouteDto withQuery(NaviWithQueryDto dto) {
        // dto에서 PointDto start (좌표)를 가져왔고,
        // String query에서 좌표를 가져와 >> 메서드로 뽑는다.
        // 이 둘의 이동경로를 구하면 된다.

        PointDto start = dto.getStart();
        PointDto goal = this.geoCoding(dto.getQuery());
        NaviWithPointsDto request = new NaviWithPointsDto(start, goal);
        NaviRouteDto result = this.directions5(request);
        return result;
    }
    //Exchange from address to coordinates
    public PointDto geoCoding(String query) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        GeoNcpResponse response = mapExchange.geocode(params);
        log.info("geoCoding: {}", response);
        if (response == null || !response.getStatus().equals("OK")) {
            throw new IllegalStateException("Error fetching geocoding data: " + response.getErrorMessage());
        }
        List<GeoAddress> addresses = response.getAddresses();
        if (addresses == null || addresses.isEmpty()) {
            throw new IllegalStateException("No addresses found for the given query.");
        }

        String x = response.getAddresses().get(0).getX();
        String y = response.getAddresses().get(0).getY();

        return new PointDto(Double.parseDouble(y), Double.parseDouble(x));
    }

    public NaviRouteDto withIpAddresses(NaviWithIpsDto dto) {
        PointDto start = this.geoLocation(dto.getStartIp());
        PointDto goal = this.geoLocation(dto.getGoalIp());
        NaviWithPointsDto request = new NaviWithPointsDto(start, goal);

        return this.directions5(request);
    }

    //IP주소를 입력하면 x,y값이 나온다.
    public PointDto geoLocation(String ip) {
        Map<String, Object> params = new HashMap<>();
        params.put("ip", ip);
        params.put("ext", "t");
        params.put("responseFormatType", "json");

        GeoLocationNcpResponse response = geoExchange.geoLocation(params);
        Double lng = response.getGeoLocation().getLng();
        Double lat = response.getGeoLocation().getLat();

        return new PointDto(lng, lat);
    }

}
