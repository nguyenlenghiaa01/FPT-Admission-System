package com.fptu.hk7.candidateservice.event;

import lombok.Data;

@Data
public class ApplicationReportEvent {

    private String applicationUuid;
    private String campusName;
    private Integer approved;
    private Integer reject;
}
