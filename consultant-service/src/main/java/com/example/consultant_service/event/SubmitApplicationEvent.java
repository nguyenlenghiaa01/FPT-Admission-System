package com.example.consultant_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitApplicationEvent {
    String email;
    String phone;
    String fullname;
    String specialization;
    String campus;
}
