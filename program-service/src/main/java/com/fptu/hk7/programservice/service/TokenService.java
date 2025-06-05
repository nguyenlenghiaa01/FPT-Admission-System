package com.fptu.hk7.programservice.service;

import com.fptu.hk7.programservice.dto.Response.AuthorResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;

@Service
public class TokenService {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    HandlerExceptionResolver resolver;

    private final String SECRET_KEY = "4bb6d1dfbafb64a681139d1586b6f1160d18159afd57c8c79136d7490630407c";

    private SecretKey getSigninKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public AuthorResponse isAuthenticated(String token) {
        System.out.println("Token received: " + token);
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigninKey()).build().parseClaimsJws(token).getBody();
        AuthorResponse authorResponse = new AuthorResponse();
        authorResponse.setEmail(claims.getSubject());
        authorResponse.setRole(claims.get("roles", String.class));
        return authorResponse;
    }
}
