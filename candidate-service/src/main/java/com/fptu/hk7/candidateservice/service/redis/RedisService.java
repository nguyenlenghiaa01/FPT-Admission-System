package com.fptu.hk7.candidateservice.service.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void testConnectionOnStartup() {
        try {
            redisTemplate.hasKey("test-key");
            System.out.println("✅ Redis connection test SUCCESS");
        } catch (Exception e) {
            System.err.println("❌ Redis connection test FAILED");
            e.printStackTrace();
        }
    }

    /**
     * Send a message to the new-application Redis channel
     * @param message The message to send
     */
    public boolean sendApplicationMessage(Object message, String chanel) {
        try {
            redisTemplate.convertAndSend("new-application", message);
            System.out.println("✅ Sent message to Redis channel 'new-application': " + message);
            return true;
        } catch (Exception e) {
            System.err.println("❌ Failed to send message to Redis channel 'new-application'");
            e.printStackTrace();
        }
        return false;
    }
}
