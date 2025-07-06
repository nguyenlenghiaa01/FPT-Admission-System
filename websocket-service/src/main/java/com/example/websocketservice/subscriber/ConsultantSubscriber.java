package com.example.websocketservice.subscriber;

import com.example.websocketservice.event.Booking1Request;
import com.example.websocketservice.event.SocketNewApplicationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultantSubscriber implements MessageListener {
    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String rawJsonString = new String(message.getBody());
            System.out.println("new-scheduler topic");
            System.out.println("Raw received JSON string: " + rawJsonString);

            String actualJson = objectMapper.readValue(rawJsonString, String.class);
            System.out.println("Actual JSON string: " + actualJson);

            Booking1Request event = objectMapper.readValue(actualJson, Booking1Request.class);
            System.out.println("Deserialized event: " + event);

            String consultantUuid = event.getStaff_uuid();
            if (consultantUuid == null || consultantUuid.isBlank()) {
                throw new IllegalArgumentException("Consultant UUID is null or empty.");
            }

            String topic = "/topic/new-scheduler/" + consultantUuid;
            template.convertAndSend(topic, event);
            System.out.println("WebSocket message sent to " + topic + ": " + actualJson);
        } catch (Exception e) {
            String fallback = new String(message.getBody());
            template.convertAndSend("/topic/new-application", fallback);
            System.out.println("Error deserializing message, sent as string: " + fallback);
            e.printStackTrace();
        }
    }
}
