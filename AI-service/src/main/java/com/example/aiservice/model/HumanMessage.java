package com.example.aiservice.model;

public record HumanMessage(String query, String history, String sessionId) {
    public HumanMessage {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Query cannot be null or empty");
        }
    }
}
