package com.example.consultant_service.event;

import jakarta.persistence.Column;
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

    private Integer bookedCount;

    private Integer processingCount;

    private Integer canceled;
}
