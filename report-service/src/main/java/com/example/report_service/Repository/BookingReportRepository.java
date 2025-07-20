package com.example.report_service.Repository;

import com.example.report_service.Entity.BookingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingReportRepository extends JpaRepository<BookingReport,String> {
    Optional<BookingReport> findByCampusNameAndMonthAndYearAndWeekOfYear(String bookingUuid,Integer month, Integer year, Integer weekOfYear);
    Optional<BookingReport> findByBookingUuidAndMonthAndYearAndWeekOfYear(
            String bookingUuid, Integer month, Integer year, Integer weekOfYear);

    @Query("SELECT b FROM BookingReport b " +
            "WHERE (:campusName IS NULL OR b.campusName = :campusName) " +
            "AND (:month IS NULL OR b.month = :month) " +
            "AND (:year IS NULL OR b.year = :year)")
    List<BookingReport> filterByOptionalFields(
            @Param("campusName") String campusName,
            @Param("month") Integer month,
            @Param("year") Integer year);

}
