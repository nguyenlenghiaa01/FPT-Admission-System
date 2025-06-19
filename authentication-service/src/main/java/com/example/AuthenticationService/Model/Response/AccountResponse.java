package com.example.AuthenticationService.Model.Response;

import com.example.AuthenticationService.Enum.Role;
import lombok.Data;

@Data
public class AccountResponse {
    String uuid;
    String userName;
    String fullName;
    String phone;
    String email;
    String address;
    Role role;
    String token;
    String image;
    String message;
}
