package com.example.websocketservice.subscriber;

import com.example.websocketservice.event.SocketNewApplicationEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateSubscriber implements MessageListener {
    private final SimpMessagingTemplate template;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String rawJsonString = new String(message.getBody());
            System.out.println("Raw received JSON string: " + rawJsonString);

            String actualJson = objectMapper.readValue(rawJsonString, String.class);

            SocketNewApplicationEvent event = objectMapper.readValue(actualJson, SocketNewApplicationEvent.class);

            String consultantUuid = event.getConsultantUuid();
            if (consultantUuid == null || consultantUuid.isBlank()) {
                throw new IllegalArgumentException("Consultant UUID is null or empty.");
            }

            String topic = "/topic/new-application/" + consultantUuid;
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
