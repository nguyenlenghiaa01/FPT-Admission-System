package com.example.websocketservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingReportEvent {
    private String bookingUuid;
    private Integer weekOfYear;
    private String campusName;
    private Integer month;
    private Integer year;
    private Integer totalBooking;
    private Integer completedCount;
    private Integer canceledCount;
}
