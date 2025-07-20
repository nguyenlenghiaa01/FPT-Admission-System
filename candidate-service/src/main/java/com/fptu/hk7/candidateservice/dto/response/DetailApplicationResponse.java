package com.fptu.hk7.candidateservice.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DetailApplicationResponse {
    // detail application
    private String scholarship;
    private String major;
    private String specialization;
    private String campus;
    // consultant
    private String fullname;
    private String email;
    private String phone;

    // booking
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yy HH:mm")
    private LocalDateTime startTime;

}
