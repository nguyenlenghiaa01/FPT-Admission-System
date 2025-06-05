package com.example.Nofication.Model.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionApplicationNotificationRequest {
    String email;
    String fullname;
    String phone;
    String campus;
    String specialization;
}
