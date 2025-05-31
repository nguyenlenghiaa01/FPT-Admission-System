package com.example.AuthenticationService.Model.Request;

import lombok.Data;

@Data
public class LoginRequest {
    String userName;
    String password;
}
