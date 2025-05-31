package com.example.AuthenticationService.Model.Request;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    String email;
    String phone;
    String fullName;
    String address;
    String image;
}
