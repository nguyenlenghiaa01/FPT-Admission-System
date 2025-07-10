package com.example.consultant_service.Model.Response;

import lombok.Data;

@Data
public class AccountResponse {
    String userName;
    String fullName;
    String phone;
    String email;
    String address;
    String image;
    String message;
}
