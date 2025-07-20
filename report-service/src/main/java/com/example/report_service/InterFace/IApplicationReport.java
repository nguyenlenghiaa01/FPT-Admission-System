package com.example.report_service.InterFace;

import com.example.report_service.DTO.Response.ApplicationReportResponse;
import com.example.report_service.Entity.ApplicationReport;

import java.util.Map;

public interface IApplicationReport {
    ApplicationReport applicationReport(Map<String, String> data);

    ApplicationReportResponse getCount();
}
