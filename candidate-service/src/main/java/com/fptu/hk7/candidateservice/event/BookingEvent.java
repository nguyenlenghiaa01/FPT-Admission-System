package com.fptu.hk7.candidateservice.event;

import lombok.Data;

@Data
public class BookingEvent {
    private String schedularUuid;
    private String candidateUuid;
}
