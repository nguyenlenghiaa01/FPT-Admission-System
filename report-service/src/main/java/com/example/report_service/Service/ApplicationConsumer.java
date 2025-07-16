package com.example.report_service.Service;

import com.example.report_service.Entity.ApplicationReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApplicationConsumer {

    private final ObjectMapper objectMapper;

    private final ApplicationReportService applicationReportService;

    @KafkaListener(topics = "application_report", groupId = "report-group")
    public void listenToApplicationReportFromCandidate(String message) {
        try {
            System.out.println("Catch application_report event: Đã nhận được event từ CandidateService");
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});

            ApplicationReport applicationReport = applicationReportService.applicationReport(data);
            System.out.println("Success");
            System.out.println(applicationReport.toString());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
