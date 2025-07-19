package com.example.report_service.Service;

import com.example.report_service.Entity.ApplicationReport;
import com.example.report_service.InterFace.IApplicationReport;
import com.example.report_service.Repository.ApplicationReportRepository;
import com.example.report_service.Service.redis.RedisService;
import com.example.report_service.event.ApplicationReportEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationReportService implements IApplicationReport {

    private final ApplicationReportRepository applicationReportRepository;
    private final RedisService redisService;
    public ApplicationReport applicationReport(Map<String, String> data) {
        String campusName = data.get("campusName");
        String applicationUuid = data.get("applicationUuid");
        if (campusName == null || applicationUuid == null) {
            throw new IllegalArgumentException("Missing campusName or applicationUuid");
        }

        Integer approved = data.containsKey("approved") ? Integer.parseInt(data.get("approved")) : 0;
        Integer reject = data.containsKey("reject") ? Integer.parseInt(data.get("reject")) : 0;

        LocalDateTime now = LocalDateTime.now();
        Integer month = now.getMonthValue();
        Integer year = now.getYear();

        Optional<ApplicationReport> optionalReport =
                applicationReportRepository.findByCampusNameAndMonthAndYear(campusName, month, year);

        Optional<ApplicationReport> optionalApplicationReport = applicationReportRepository.findByApplicationUuidAndMonthAndYear(applicationUuid,month,year);
        ApplicationReport report;

        if (optionalApplicationReport.isPresent()) {
            report = optionalApplicationReport.get();
        } else if (optionalReport.isPresent()) {
            report = optionalReport.get();
        }else {
            // Khong co hoac sang thang moi → tạo mới
            report = new ApplicationReport();
            report.setUuid(UUID.randomUUID().toString());
            report.setCampusName(campusName);
            report.setApplicationUuid(applicationUuid);
            report.setMonth(month);
            report.setYear(year);
            report.setTotalApplication(0);
            report.setApproveCount(0);
            report.setRejectCount(0);
        }

        if (approved == 0 && reject == 0) {
            report.setTotalApplication(report.getTotalApplication() + 1);
        }
        if (approved == 1) {
            report.setApproveCount(report.getApproveCount() + 1);
        }
        if (reject == 1) {
            report.setRejectCount(report.getRejectCount() + 1);
        }

        applicationReportRepository.save(report);

        // WebSocket
        ApplicationReportEvent event = ApplicationReportEvent.builder()
                .campusName(report.getCampusName())
                .applicationUuid(report.getApplicationUuid())
                .totalApplication(report.getTotalApplication())
                .approveCount(report.getApproveCount())
                .rejectCount(report.getRejectCount())
                .month(report.getMonth())
                .year(report.getYear())
                .build();

        redisService.sendApplicationMessage(event, "/topic/new-application-report/");
        return report;
    }


}
