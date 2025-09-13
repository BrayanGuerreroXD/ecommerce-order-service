package com.test.ecommerceorderservice.application.port.out;

public interface NotificationPort {
    void sendNotification(String to, String message);
}

