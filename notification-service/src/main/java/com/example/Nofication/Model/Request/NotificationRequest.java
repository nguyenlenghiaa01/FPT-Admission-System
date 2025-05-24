package com.example.Nofication.Model.Request;

import com.example.Nofication.Enum.StatusEnum;
import com.example.Nofication.Enum.TypeEnum;
import jakarta.persistence.Column;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    @Email(message = "Invalid Email!")
    private String email;
    private String token;

}
