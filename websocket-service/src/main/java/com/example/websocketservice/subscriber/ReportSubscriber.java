package com.example.websocketservice.subscriber;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportSubscriber implements MessageListener {

    private final SimpMessagingTemplate template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String eventType = new String(message.getBody());
            System.out.println("Received raw Redis message: " + eventType);

            String topic = "/topic/report";

            switch (eventType) {
                case "new-user" -> {
                    template.convertAndSend(topic, "new-user");
                    System.out.println("Sent WebSocket message: new-user");
                }
                case "booking-updated-status" -> {
                    template.convertAndSend(topic, "booking-updated-status");
                    System.out.println("Sent WebSocket message: booking-updated-status");
                }
                case "application-updated-status" -> {
                    template.convertAndSend(topic, "application-updated-status");
                    System.out.println("Sent WebSocket message: application-updated-status");
                }
                default -> {
                    template.convertAndSend(topic, "unknown-event");
                    System.out.println("Sent WebSocket message: unknown-event");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
