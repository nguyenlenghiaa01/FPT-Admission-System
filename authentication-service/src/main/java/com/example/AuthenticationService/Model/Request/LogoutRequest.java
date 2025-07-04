package com.example.AuthenticationService.Model.Request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LogoutRequest {
    @NotNull(message = "User uuid cannot be null!")
    private String userUuid;
    @NotNull(message = "Token JWT cannot be null!")
    private String token;
}
