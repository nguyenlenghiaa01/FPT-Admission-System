package com.fptu.hk7.candidateservice.dto.response;

import com.fptu.hk7.candidateservice.enums.Role;
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
}
