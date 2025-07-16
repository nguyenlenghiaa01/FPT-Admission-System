package com.example.websocketservice.subscriber;

import com.example.websocketservice.event.ApplicationReportEvent;
import com.example.websocketservice.event.SocketNewApplicationEvent;
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
            System.out.println("new-application-report topic");
            System.out.println("Raw received JSON string: " + rawJsonString);

            // Bóc lớp JSON string lồng bên ngoài
            String actualJson = objectMapper.readValue(rawJsonString, String.class);

            // Parse thành đối tượng event
            ApplicationReportEvent event = objectMapper.readValue(actualJson, ApplicationReportEvent.class);

            // Lấy thông tin định danh để gửi đúng topic
            String campusName = event.getCampusName();
            if (campusName == null || campusName.isBlank()) {
                throw new IllegalArgumentException("Campus name is missing");
            }

            String topic = "/topic/new-application-report/" + campusName;
            template.convertAndSend(topic, event);
            System.out.println("WebSocket message sent to " + topic + ": " + actualJson);

        } catch (Exception e) {
            String fallback = new String(message.getBody());
            template.convertAndSend("/topic/new-application-report", fallback);
            System.out.println("Error deserializing message, sent as string: " + fallback);
            e.printStackTrace();
        }
    }

}
