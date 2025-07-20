package com.fptu.hk7.candidateservice.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReturnApplication {
    private String booking_id;
    private String status;
    private String consultantUuid;
    private String note;
    private LocalDateTime startTime;
}
