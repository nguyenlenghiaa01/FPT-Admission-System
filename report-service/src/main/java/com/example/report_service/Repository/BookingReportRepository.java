package com.example.report_service.Repository;

import com.example.report_service.Entity.BookingReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingReportRepository extends JpaRepository<BookingReport,String> {
    Optional<BookingReport> findByCampusNameAndMonthAndYearAndWeekOfYear(String bookingUuid,Integer month, Integer year, Integer weekOfYear);
    Optional<BookingReport> findByBookingUuidAndMonthAndYearAndWeekOfYear(
            String bookingUuid, Integer month, Integer year, Integer weekOfYear);
}
