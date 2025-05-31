package com.example.apigateway.Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenUtil {

    private static final String SECRET = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    public Claims parseToken(String token) {
        System.out.println(">> Token nhận được: " + token);
        if (token == null || !token.contains(".") || token.split("\\.").length != 3) {
            throw new IllegalArgumentException("Token không hợp lệ: phải có đúng 2 dấu chấm.");
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new RuntimeException("Không thể phân tích token: " + e.getMessage(), e);
        }
    }

    public String extractEmail(String token) {
        return parseToken(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = parseToken(token).getExpiration();
        return expiration.before(new Date());
    }


    public SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String extractRole(String token) {
        return parseToken(token).get("role", String.class);
    }

}
