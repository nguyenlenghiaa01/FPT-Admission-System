package com.example.report_service.InterFace;

import com.example.report_service.Entity.UserReport;

import java.util.Map;

public interface IUserReport {
    UserReport userReport(Map<String, String> data);
}
