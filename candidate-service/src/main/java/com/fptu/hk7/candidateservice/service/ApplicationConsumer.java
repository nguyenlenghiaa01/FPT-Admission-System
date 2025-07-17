package com.fptu.hk7.candidateservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fptu.hk7.candidateservice.InterFace.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationConsumer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final IApplicationService applicationService;

    @KafkaListener(topics = "return_application_submit", groupId = "candidate-group")
    public void listenToUpdateStatusApplication(String message) {
        try {
            System.out.println("Catch booking-report event: Đã nhận được event từ ConsultantService");
            // Xử lý message ở đây
            System.out.println("Message received: " + message);
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});
            applicationService.returnStatusApplication(data);
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
