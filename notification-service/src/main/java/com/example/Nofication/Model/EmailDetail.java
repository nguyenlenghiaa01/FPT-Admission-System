package com.example.Nofication.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EmailDetail {
    String receiver;
    String subject;

    // forgot-password
    String link;

    // application submission
    String fullname;
    String phone;
    String specialization;
    String campus;
}
