package com.example.report_service.Repository;

import com.example.report_service.Entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport,String> {
    Optional<UserReport> findByMonthAndYearAndWeekOfYear(int month, int year, int weekOfYear);

    @Query("SELECT u FROM UserReport u " +
            "WHERE (:month IS NULL OR u.month = :month) " +
            "OR (:year IS NULL OR u.year = :year) " +
            "OR (:weekOfYear IS NULL OR u.weekOfYear = :weekOfYear)")
    List<UserReport> filter(Integer weekOfYear, Integer month, Integer year);
}
