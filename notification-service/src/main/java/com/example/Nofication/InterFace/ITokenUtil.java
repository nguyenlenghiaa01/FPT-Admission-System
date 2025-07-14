package com.example.Nofication.InterFace;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;

public interface ITokenUtil {
    Claims parseToken(String token);
    String extractEmail(String token);
    String getToken(HttpServletRequest request);
    String extractRole(String token);
}
