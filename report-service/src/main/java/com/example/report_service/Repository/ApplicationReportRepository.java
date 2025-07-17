package com.example.report_service.Repository;

import com.example.report_service.Entity.ApplicationReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationReportRepository extends JpaRepository<ApplicationReport,String> {
    Optional<ApplicationReport> findByCampusNameAndMonthAndYear(String campus,Integer month, Integer year);
    Optional<ApplicationReport> findByApplicationUuidAndMonthAndYear(String uuid, Integer month, Integer year);
}
