package com.fptu.hk7.candidateservice.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReturnApplication {
    private String booking_id;
    private String status;
    private String note;
}
