package com.example.report_service.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ApplicationReport {

    @Id
    private String uuid;

    @Column(name="application_uuid")
    private String applicationUuid;

    private Integer month;

    private Integer year;

    @Column(name="campus_name")
    private String campusName;

    @Column(name="total_application")
    private Integer totalApplication = 0;

    @Column(name="approve_count")
    private Integer approveCount = 0;

    @Column(name="reject_count")
    private Integer rejectCount = 0;
}
