package com.example.report_service.InterFace;

import com.example.report_service.DTO.Response.BookingReportResponse;
import com.example.report_service.Entity.BookingReport;

import java.util.List;
import java.util.Map;

public interface IBookingReport {
    BookingReport bookingReport(Map<String, String> data);

    BookingReportResponse getCount();
    List<BookingReport> filter(String campus, Integer month, Integer year);
}
