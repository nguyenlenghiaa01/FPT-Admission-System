package com.example.report_service.DTO.Response;

import lombok.Data;

@Data
public class BookingReportResponse {
    private Integer completedCount;

    private Integer canceledCount;
}
