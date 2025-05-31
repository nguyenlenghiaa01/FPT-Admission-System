package com.example.AuthenticationService.Model.Request;

import com.example.AuthenticationService.Enum.Role;

public class UserDTO {
    String uuid;
    String userName;
    String fullName;
    String phone;
    String email;
    String address;
    Role role;
    String token;
    String image;
}
