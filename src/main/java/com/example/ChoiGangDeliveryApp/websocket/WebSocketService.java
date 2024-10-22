package com.example.ChoiGangDeliveryApp.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    // Gửi thông báo trạng thái đơn hàng tới user
    //send notification to user
    public void sendOrderStatusToUser(String userId, String message) {
        messagingTemplate.convertAndSend("/user/" + userId + "/notifications", message);
    }

    // gửi thông báo trạng thái đơn hàng tới của hàng
    //send notification to restaurant
    public void sendOrderStatusToRestaurant(String restaurantId, String message) {
        messagingTemplate.convertAndSend("/restaurant/" + restaurantId + "/notifications", message);
    }



    // gửi một số thông báo khác tại đây nếu muốn
    //other notification



}
