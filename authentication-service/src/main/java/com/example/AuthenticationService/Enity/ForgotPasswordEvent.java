package com.example.AuthenticationService.Enity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordEvent {
    private String email;
    private String token;
}
