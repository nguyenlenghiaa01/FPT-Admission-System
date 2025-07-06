package com.example.AuthenticationService.Service.redis;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenService {
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

    public void saveToken(String key, String token, long ttlMinutes) {
        redisTemplate.opsForValue().set(key, token, Duration.ofMinutes(ttlMinutes));
    }

    public String getToken(String key) {
        Object token = redisTemplate.opsForValue().get(key);
        return token != null ? token.toString() : null;
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }

    public boolean isTokenValid(String key, String token) {
        String stored = getToken(key);
        return stored != null && stored.equals(token);
    }

    /**
     * Đưa jti vào blacklist (logout)
     *
     * @param jwt         ID duy nhất của token
     * @param ttlSeconds  Thời gian sống còn lại của token (giây)
     */
    public void blacklistToken(String jwt, long ttlSeconds) {
        String key = "blacklist:" + jwt;
        redisTemplate.opsForValue().set(key, "1", Duration.ofSeconds(ttlSeconds));
    }

    /**
     * Kiểm tra token đã bị revoke chưa
     *
     * @param jwt ID duy nhất của token
     * @return true nếu token đã bị revoke
     */
    public boolean isTokenRevoked(String jwt) {
        String key = "blacklist:" + jwt;
        return redisTemplate.hasKey(key);
    }
}
