package com.example.report_service.Service;

import com.example.report_service.Entity.BookingReport;
import com.example.report_service.Repository.BookingReportRepository;
import com.example.report_service.Service.redis.RedisService;
import com.example.report_service.event.ApplicationReportEvent;
import com.example.report_service.event.BookingReportEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingReportService {

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

        Optional<BookingReport> optionalBookingReport = bookingReportRepository.findByBookingUuidAndMonthAndYearAndWeekOfYear(bookingUuid,month,year,weekOfYear);
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
        BookingReportEvent event = BookingReportEvent.builder()
                .campusName(report.getCampusName())
                .bookingUuid(report.getBookingUuid())
                .weekOfYear(report.getWeekOfYear())
                .month(report.getMonth())
                .year(report.getYear())
                .canceledCount(report.getCanceledCount())
                .completedCount(report.getCompletedCount())
                .totalBooking(report.getTotalBooking())
                .build();

        redisService.sendApplicationMessage(event,"/topic/new-booking-report/" );

        return report;
    }
}
