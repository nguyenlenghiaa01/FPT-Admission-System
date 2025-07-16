package com.example.report_service.Service;

import com.example.report_service.Entity.BookingReport;
import com.example.report_service.Repository.BookingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingReportService {

    private final BookingReportRepository bookingReportRepository;

    public BookingReport bookingReport(Map<String, String> data) {
        String campusName = data.get("campusName");
        String bookingUuid = data.get("bookingUuid");

        LocalDateTime now = LocalDateTime.now();
        int month = now.getMonthValue();
        int year = now.getYear();

        // Tính số tuần trong năm
        int weekOfYear = now.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        Optional<BookingReport> optionalReport =
                bookingReportRepository.findByCampusNameAndMonthAndYearAndWeekOfYear(campusName, month, year, weekOfYear);

        BookingReport report;

        if (optionalReport.isPresent()) {
            report = optionalReport.get();
        } else {
            report = new BookingReport();
            report.setCampusName(campusName);
            report.setBookingUuid(bookingUuid);
            report.setMonth(month);
            report.setYear(year);
            report.setWeekOfYear(weekOfYear);
            report.setTotalBooking(0);
        }

        report.setTotalBooking(report.getTotalBooking() + 1);
        return bookingReportRepository.save(report);
    }
}
