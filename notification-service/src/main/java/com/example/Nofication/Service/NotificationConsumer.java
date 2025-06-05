package com.example.Nofication.Service;

import com.example.Nofication.Model.Request.NotificationRequest;
import com.example.Nofication.Model.Request.SubmissionApplicationNotificationRequest;
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

    @KafkaListener(topics = "submit_application", groupId = "notification-group")
    public void listenToSubmitApplicationEvent(String message) {
        try {
            // log
            System.out.println("Nhận được kafka event: Submission_Application");

            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});
            String email = data.get("email");
            String fullname = data.get("fullname");
            String phone = data.get("phone");
            String campus = data.get("campus");
            String specialization = data.get("specialization");

            SubmissionApplicationNotificationRequest request = new SubmissionApplicationNotificationRequest(
                    email, fullname, phone, campus, specialization
            );

            notificationService.createNotificationSubmitApplication(request);
        } catch (Exception e){
            System.err.println("Failed to process Kafka message of notifying application submitted: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


