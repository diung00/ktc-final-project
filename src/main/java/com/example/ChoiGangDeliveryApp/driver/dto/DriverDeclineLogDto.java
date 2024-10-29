package com.example.ChoiGangDeliveryApp.driver.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverDeclineLogDto {
    private Long driverId;
    private Long orderId;
    private String reason;


}
