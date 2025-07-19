package com.example.report_service.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserReportEvent {
    private int year;
    private int month;
    private int weekOfYear;
    private int newUser;
}
