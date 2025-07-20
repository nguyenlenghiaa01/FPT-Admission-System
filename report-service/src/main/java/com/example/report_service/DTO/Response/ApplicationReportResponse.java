package com.example.report_service.DTO.Response;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ApplicationReportResponse {
    private Integer totalApplication;

    private Integer approveCount;

    private Integer rejectCount;
}
