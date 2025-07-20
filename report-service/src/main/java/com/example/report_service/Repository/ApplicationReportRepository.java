package com.example.report_service.Repository;

import com.example.report_service.Entity.ApplicationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApplicationReportRepository extends JpaRepository<ApplicationReport,String> {
    Optional<ApplicationReport> findByCampusNameAndMonthAndYear(String campus,Integer month, Integer year);
    Optional<ApplicationReport> findByApplicationUuidAndMonthAndYear(String uuid, Integer month, Integer year);

    @Query("SELECT a FROM ApplicationReport a WHERE " +
            "(:campusName IS NULL OR a.campusName = :campusName) OR " +
            "(:month IS NULL OR a.month = :month) OR " +
            "(:year IS NULL OR a.year = :year)")
    List<ApplicationReport> findByCampusOrMonthOrYear(
            @Param("campusName") String campusName,
            @Param("month") Integer month,
            @Param("year") Integer year);

}
