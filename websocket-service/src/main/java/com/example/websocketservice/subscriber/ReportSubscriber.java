package com.example.websocketservice.subscriber;

import com.example.websocketservice.event.ApplicationReportEvent;
import com.example.websocketservice.event.BookingReportEvent; // 👈 Import thêm
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportSubscriber implements MessageListener {

    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String rawJsonString = new String(message.getBody());
            System.out.println("Received topic message");
            System.out.println("Raw received JSON string: " + rawJsonString);

            // Parse lớp JSON string lồng bên ngoài
            String actualJson = objectMapper.readValue(rawJsonString, String.class);

            // Check nội dung JSON để xác định loại event
            if (actualJson.contains("\"applicationUuid\"")) {
                ApplicationReportEvent event = objectMapper.readValue(actualJson, ApplicationReportEvent.class);
                String campusName = event.getCampusName();
                if (campusName == null || campusName.isBlank()) {
                    throw new IllegalArgumentException("Campus name is missing in application event");
                }

                String topic = "/topic/report-channel/new-application-report/" + campusName;
                template.convertAndSend(topic, event);
                System.out.println("WebSocket message sent to " + topic + ": " + actualJson);

            } else if (actualJson.contains("\"bookingUuid\"")) {
                BookingReportEvent event = objectMapper.readValue(actualJson, BookingReportEvent.class);
                String campusName = event.getCampusName();
                if (campusName == null || campusName.isBlank()) {
                    throw new IllegalArgumentException("Campus name is missing in booking event");
                }

                String topic = "/topic/report-channel/new-booking-report/" + campusName;
                template.convertAndSend(topic, event);
                System.out.println("WebSocket message sent to " + topic + ": " + actualJson);
            }

        } catch (Exception e) {
            String fallback = new String(message.getBody());
            template.convertAndSend("/topic/new-application-report", fallback); // fallback topic chung
            System.out.println("Error deserializing message, sent as string: " + fallback);
            e.printStackTrace();
        }
    }
}
