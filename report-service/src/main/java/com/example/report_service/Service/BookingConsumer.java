package com.example.report_service.Service;

import com.example.report_service.Entity.ApplicationReport;
import com.example.report_service.Entity.BookingReport;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingConsumer {


    private final ObjectMapper objectMapper;

    private final BookingReportService bookingReportService;

    @KafkaListener(topics = "booking_report", groupId = "booking-group")
    public void listenToApplicationReportFromCandidate(String message) {
        try {
            System.out.println("Catch booking_report event: Đã nhận được event từ Consultant Service");
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});

            BookingReport bookingReport =bookingReportService.bookingReport(data);
            System.out.println("Success");
            System.out.println(bookingReport.toString());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
