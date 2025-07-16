package com.example.report_service.Repository;

import com.example.report_service.Entity.UserReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReportRepository extends JpaRepository<UserReport,String> {
}
