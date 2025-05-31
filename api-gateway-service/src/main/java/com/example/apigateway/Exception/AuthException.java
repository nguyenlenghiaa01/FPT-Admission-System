package com.example.apigateway.Exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
