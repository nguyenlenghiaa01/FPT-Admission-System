package com.fptu.hk7.candidateservice.event;

import lombok.Data;

@Data
public class BookingEvent {
    private String bookingUuid;
    private String candidateUuid;

    String email;
    String phone;
    String fullname;
    String specialization;
    String campus;
}
