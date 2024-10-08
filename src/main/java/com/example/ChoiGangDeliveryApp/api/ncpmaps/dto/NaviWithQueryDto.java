package com.example.ChoiGangDeliveryApp.api.ncpmaps.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NaviWithQueryDto {
    private PointDto start;
    private String query;

}
