package com.example.report_service.Service;

import com.example.report_service.DTO.Response.ApplicationReportResponse;
import com.example.report_service.DTO.Response.BookingReportResponse;
import com.example.report_service.Entity.ApplicationReport;
import com.example.report_service.Entity.BookingReport;
import com.example.report_service.InterFace.IBookingReport;
import com.example.report_service.Repository.BookingReportRepository;
import com.example.report_service.Service.redis.RedisService;
import com.example.report_service.event.ApplicationReportEvent;
import com.example.report_service.event.BookingReportEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookingReportService implements IBookingReport {

    private final BookingReportRepository bookingReportRepository;
    private final RedisService redisService;

    public BookingReport bookingReport(Map<String, String> data) {
        String campusName = data.get("campusName");
        String bookingUuid = data.get("bookingUuid");
        if (campusName == null || bookingUuid == null) {
            throw new IllegalArgumentException("Missing campusName or bookingUuid");
        }

        int booked = data.containsKey("bookedCount") ? Integer.parseInt(data.get("bookedCount")) : 0;
        int canceled = data.containsKey("canceled") ? Integer.parseInt(data.get("canceled")) : 0;
        int completed = data.containsKey("completedCount") ? Integer.parseInt(data.get("completedCount")) : 0;

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();
        int weekOfYear = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        Optional<BookingReport> optionalReport =
                bookingReportRepository.findByCampusNameAndMonthAndYearAndWeekOfYear(campusName, month, year, weekOfYear);

        Optional<BookingReport> optionalBookingReport =
                bookingReportRepository.findByBookingUuidAndMonthAndYearAndWeekOfYear(bookingUuid, month, year, weekOfYear);

        BookingReport report;

        if (optionalBookingReport.isPresent()) {
            report = optionalBookingReport.get();
        } else if (optionalReport.isPresent()) {
            report = optionalReport.get();
        } else {
            report = new BookingReport();
            report.setUuid(UUID.randomUUID().toString());
            report.setCampusName(campusName);
            report.setBookingUuid(bookingUuid);
            report.setMonth(month);
            report.setYear(year);
            report.setWeekOfYear(weekOfYear);
            report.setTotalBooking(0);
            report.setCanceledCount(0);
            report.setCompletedCount(0);
        }

        if (booked == 1) {
            report.setTotalBooking(report.getTotalBooking() + 1);
        }
        if (canceled == 1) {
            report.setCanceledCount(report.getCanceledCount() + 1);
        }
        if (completed == 1) {
            report.setCompletedCount(report.getCompletedCount() + 1);
        }

        bookingReportRepository.save(report);

        redisService.sendApplicationMessage("booking-updated-status", "/topic/new-booking-report");

        return report;
    }

    public BookingReportResponse getCount() {
        List<BookingReport> bookingReports = bookingReportRepository.findAll();

        int completedCount = 0;
        int canceledCount = 0;

        for (BookingReport report : bookingReports) {
            completedCount += report.getCompletedCount();
            canceledCount += report.getCanceledCount();
        }

        BookingReportResponse response = new BookingReportResponse();
        response.setCanceledCount(completedCount);
        response.setCompletedCount(canceledCount);

        return response;
    }

    public List<BookingReport> filter(String campus,Integer month, Integer year){
        return bookingReportRepository.filterByOptionalFields(campus, month, year);
    }

}
