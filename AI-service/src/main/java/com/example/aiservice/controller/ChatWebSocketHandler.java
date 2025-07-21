package com.example.aiservice.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatClient ollamaChatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Received message: {}", message.getPayload());
        JsonNode json = objectMapper.readTree(message.getPayload());
        String query = json.get("query").asText();
        String sessionId = json.has("sessionId") ? json.get("sessionId").asText() : "default";

        Flux<String> stream = ollamaChatClient
                .prompt()
                .user(query)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, sessionId))
                .stream()
                .content();

        stream
                .doOnNext(chunk -> {
                    try {
                        session.sendMessage(new TextMessage(chunk));
                    } catch (Exception e) {
                        log.error("Error sending chunk", e);
                    }
                })
                .doOnComplete(() -> log.info("Streaming complete"))
                .subscribe();
    }
}

