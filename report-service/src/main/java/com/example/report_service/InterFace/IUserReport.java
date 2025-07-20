package com.example.report_service.InterFace;

import com.example.report_service.DTO.Response.UserReportResponse;
import com.example.report_service.Entity.UserReport;

import java.util.List;
import java.util.Map;

public interface IUserReport {
    UserReport userReport(Map<String, String> data);

    UserReportResponse getCount();
    List<UserReport> filter (Integer weakOfYear, Integer month, Integer year);
}
