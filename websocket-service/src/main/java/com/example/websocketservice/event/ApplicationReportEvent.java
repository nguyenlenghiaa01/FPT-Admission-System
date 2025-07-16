package com.example.websocketservice.event;

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
}
