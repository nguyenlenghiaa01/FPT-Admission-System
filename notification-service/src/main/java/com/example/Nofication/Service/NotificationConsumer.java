package com.example.Nofication.Service;

import com.example.Nofication.Enum.TypeEnum;
import com.example.Nofication.Model.Request.NotificationRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NotificationService notificationService;

    @KafkaListener(topics = "forgot-password-events", groupId = "notification-group")
    public void listen(String message) {
        try {
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});
            String email = data.get("email");
            String token = data.get("token");

            NotificationRequest request = new NotificationRequest();
            request.setEmail(email);
            request.setToken(token);

            notificationService.createNotificationForgotPassword(request);
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


