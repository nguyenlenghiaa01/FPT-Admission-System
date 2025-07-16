package com.example.report_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationReportEvent {
    private Integer totalApplication;
    private Integer approveCount;
    private Integer rejectCount;
    private String campusName;
    private Integer month;
    private Integer year;
}
