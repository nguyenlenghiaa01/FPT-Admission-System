package com.example.report_service.Service;

import com.example.report_service.Entity.BookingReport;
import com.example.report_service.Entity.UserReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserReportConsumer {
    private final ObjectMapper objectMapper;

    private final UserReportService userReportService;

    @KafkaListener(topics = "user_report", groupId = "report-group")
    public void listenToApplicationReportFromCandidate(String message) {
        try {
            System.out.println("Catch user_report event: Đã nhận được event từ User Service");
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});

            UserReport userReport =userReportService.userReport(data);
            System.out.println("Success");
            System.out.println(userReport.toString());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
