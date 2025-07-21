package com.example.aiservice.controller;

import com.example.aiservice.model.HumanMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
@Slf4j
public class ChatBotController {

    private final ChatClient ollamaChatClient;

    @PostMapping("/web/inference")
    public Flux<String> askWeb(@RequestBody HumanMessage humanMessage) {
        log.info("Received message: {}", humanMessage);
        return this.ollamaChatClient
                .prompt()
                .user(humanMessage.query())
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,
                        humanMessage.sessionId()))
                .stream()
                .content();
    }

    @PostMapping("/inference")
    public Mono<String> askMobile(@RequestBody HumanMessage humanMessage) {
        log.info("Received message: {}", humanMessage);
        return this.ollamaChatClient
                .prompt()
                .user(humanMessage.query())
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, humanMessage.sessionId()))
                .stream()
                .content()
                .reduce("", (acc, next) -> acc + next);
    }
}
