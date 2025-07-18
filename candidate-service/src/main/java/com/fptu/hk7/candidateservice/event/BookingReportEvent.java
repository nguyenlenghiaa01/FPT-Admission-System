package com.fptu.hk7.candidateservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingReportEvent {
    private String bookingUuid;

    private String campusName;

    private String staffUuid;

    private Integer totalBooking;

    private Integer completedCount;

    private Integer pendingCount;
}
