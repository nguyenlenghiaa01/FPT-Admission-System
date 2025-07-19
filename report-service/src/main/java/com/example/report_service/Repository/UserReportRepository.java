package com.example.report_service.Repository;

import com.example.report_service.Entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserReportRepository extends JpaRepository<UserReport,String> {
    Optional<UserReport> findByMonthAndYearAndWeekOfYear(int month, int year, int weekOfYear);
}
