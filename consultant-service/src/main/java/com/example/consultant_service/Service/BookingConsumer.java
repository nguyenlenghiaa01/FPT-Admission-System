package com.example.consultant_service.Service;

import com.example.consultant_service.Entity.Booking;
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

    private final BookingService bookingService;

    @KafkaListener(topics = "booking_admission", groupId = "consultant-group")
    public void listenToBookingFromCandidate(String message) {
        try {
            System.out.println("Catch booking_admission event: Đã nhận được event từ CandidateService");
            Map<String, String> data = objectMapper.readValue(message, new TypeReference<>() {});
            String bookingUuid = data.get("bookingUuid");
            String candidateUuid = data.get("candidateUuid");

            Booking booking = bookingService.candidateBookingAdmission(bookingUuid, candidateUuid);
            System.out.println("Success: Đã tạo được lịch hẹn");
            System.out.println(booking.toString());
        } catch (Exception e) {
            System.err.println("Failed to process Kafka message: " + e.getMessage());
        }
    }
}
